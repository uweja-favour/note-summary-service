package com.xapps.note_summary.application.generation

import com.xapps.model.DeliveryStatus
import com.xapps.note_summary.application.useronline.policy.DeliverPendingNoteSummaryPolicy
import com.xapps.note_summary.domain.NoteSummaryFactory
import com.xapps.note_summary.domain.model.PendingNoteSummary
import com.xapps.note_summary.domain.repository.NoteSummaryDraftRepository
import com.xapps.note_summary.domain.repository.NoteSummaryRepository
import com.xapps.note_summary.domain.repository.PendingNoteSummaryRepository
import com.xapps.note_summary.infrastructure.clock.NoteSummaryClockProvider
import com.xapps.platform.core.outcome.onFailure
import com.xapps.platform.core.outcome.outcomeOf
import com.xapps.platform.core.string.generateUniqueId
import org.springframework.stereotype.Component

@Component
class NoteSummaryGenerationCompletionHandler(
    private val draftRepository: NoteSummaryDraftRepository,
    private val noteSummaryRepository: NoteSummaryRepository,
    private val pendingRepository: PendingNoteSummaryRepository,
    private val clock: NoteSummaryClockProvider,
    private val deliveryPolicy: DeliverPendingNoteSummaryPolicy,
    private val noteSummaryFactory: NoteSummaryFactory
) {

    suspend fun complete(
        noteSummaryId: String,
        userId: String,
        content: String
    ) = outcomeOf {

        val draft = draftRepository.findByNoteSummaryId(noteSummaryId)
            ?: error("Note summary $noteSummaryId not found")

        val summary = noteSummaryFactory.create(
            id = noteSummaryId,
            fileName = draft.fileName,
            fileType = draft.fileType,
            content = content,
            style = draft.style,
            now = clock.now()
        )

        noteSummaryRepository.save(summary)

        pendingRepository.save(
            PendingNoteSummary(
                id = generateUniqueId(),
                noteSummaryId = noteSummaryId,
                userId = userId,
                status = DeliveryStatus.PENDING,
                createdAt = clock.now()
            )
        )

        draftRepository.markCompleted(noteSummaryId)

        deliveryPolicy.execute(userId)

    }.onFailure { ex ->

        draftRepository.markFailed(noteSummaryId)

        throw ex.exception
    }
}