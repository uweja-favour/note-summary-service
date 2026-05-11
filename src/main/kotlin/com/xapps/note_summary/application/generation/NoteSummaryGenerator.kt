package com.xapps.note_summary.application.generation

import com.xapps.model.DeliveryStatus
import com.xapps.model.FileType
import com.xapps.note_summary.application.useronline.policy.DeliverPendingNoteSummaryPolicy
import com.xapps.note_summary.domain.NoteSummaryFactory
import com.xapps.note_summary.domain.model.NoteSummaryStyle
import com.xapps.note_summary.domain.model.PendingNoteSummary
import com.xapps.note_summary.domain.repository.NoteSummaryDraftRepository
import com.xapps.note_summary.domain.repository.NoteSummaryRepository
import com.xapps.note_summary.domain.repository.PendingNoteSummaryRepository
import com.xapps.note_summary.infrastructure.ai_text_cleaner.AiTextCleaner
import com.xapps.note_summary.infrastructure.claude_service.ClaudeService
import com.xapps.note_summary.infrastructure.clock.NoteSummaryClockProvider
import com.xapps.note_summary.infrastructure.file_text_extractor.FileTextExtractorService
import com.xapps.note_summary.infrastructure.object_store.ObjectKey
import com.xapps.note_summary.infrastructure.object_store.asExtractableFile
import com.xapps.note_summary.infrastructure.prompt_builder.NoteSummaryPromptBuilderService
import com.xapps.platform.core.outcome.onFailure
import com.xapps.platform.core.outcome.outcomeOf
import com.xapps.platform.core.string.generateUniqueId
import org.springframework.stereotype.Component

data class GenerateNoteSummaryCommand(
    val noteSummaryId: String,
    val fileKey: ObjectKey,
    val style: NoteSummaryStyle,
    val userId: String,
    val fileName: String,
    val fileType: FileType
)

@Component
class NoteSummaryGenerator(
    private val fileProcessor: FileProcessor,
    private val textExtractor: FileTextExtractorService,
    private val promptBuilder: NoteSummaryPromptBuilderService,
    private val claudeService: ClaudeService,
    private val draftRepository: NoteSummaryDraftRepository,
    private val noteSummaryRepository: NoteSummaryRepository,
    private val pendingRepository: PendingNoteSummaryRepository,
    private val clock: NoteSummaryClockProvider,
    private val deliveryPolicy: DeliverPendingNoteSummaryPolicy,
    private val noteSummaryFactory: NoteSummaryFactory
) {

    suspend fun generate(command: GenerateNoteSummaryCommand) {
        outcomeOf {
            val handle = fileProcessor.fetch(command.fileKey)
            val extractedText = textExtractor.extractText(handle.asExtractableFile()).value

            val prompt = promptBuilder.buildPrompt(
                note = extractedText,
                style = command.style
            )

            val content = claudeService.generateStringResponse(
                prompt,
            ).let {
                AiTextCleaner.clean(it)
            }

            val summary = noteSummaryFactory.create(
                id = command.noteSummaryId,
                fileName = command.fileName,
                fileType = command.fileType,
                content = content,
                style = command.style,
                now = clock.now()
            )

            noteSummaryRepository.save(summary)

            pendingRepository.save(
                PendingNoteSummary(
                    id = generateUniqueId(),
                    noteSummaryId = command.noteSummaryId,
                    userId = command.userId,
                    status = DeliveryStatus.PENDING,
                    createdAt = clock.now()
                )
            )

            draftRepository.markCompleted(command.noteSummaryId)

            deliveryPolicy.execute(command.userId)

        }.onFailure { ex ->

            draftRepository.markFailed(command.noteSummaryId)

            throw ex.exception
        }
    }
}