package com.xapps.note_summary.application.generation

import com.xapps.model.TaskDraftStatus
import com.xapps.model.toFileType
import com.xapps.note_summary.api.dto.CreateNoteSummaryRequest
import com.xapps.note_summary.domain.model.NoteSummaryDraft
import com.xapps.note_summary.domain.repository.NoteSummaryDraftRepository
import com.xapps.platform.core.string.generateUniqueId
import org.springframework.stereotype.Component

@Component
class NoteSummaryDraftService(
    private val draftRepository: NoteSummaryDraftRepository,
    private val fileProcessor: FileProcessor
) {

    suspend fun createDraft(
        userId: String,
        request: CreateNoteSummaryRequest
    ): NoteSummaryDraft {

        val fileKey = fileProcessor.upload(request.file)

        val noteSummaryId = generateUniqueId()

        val draft = NoteSummaryDraft(
            id = generateUniqueId(),
            noteSummaryId = noteSummaryId,
            fileName = request.file.fileName,
            fileType = request.file.encodedFile.toFileType(),
            status = TaskDraftStatus.IN_PROGRESS,
            style = request.style,
            userId = userId,
            fileKey = fileKey
        )

        draftRepository.save(draft)
        return draft
    }
}