package com.xapps.note_summary.application.ack

import com.xapps.model.DeliveryStatus
import com.xapps.model.TaskDraftStatus
import com.xapps.note_summary.domain.repository.NoteSummaryDraftRepository
import com.xapps.note_summary.domain.repository.PendingNoteSummaryRepository
import org.springframework.stereotype.Component

@Component
class NoteSummaryAcknowledgementService(
    private val pendingRepository: PendingNoteSummaryRepository,
    private val draftRepository: NoteSummaryDraftRepository,
) {

    suspend fun acknowledgeNoteSummary(
        noteSummaryId: String
    ) {
        val pendingNoteSummary = pendingRepository.findByNoteSummaryId(noteSummaryId)
            ?: return

        pendingRepository.save(pendingNoteSummary.copy(status = DeliveryStatus.DELIVERED))

        val draft = draftRepository.findByNoteSummaryId(noteSummaryId = noteSummaryId)
            ?: return

        draftRepository.save(draft.copy(status = TaskDraftStatus.COMPLETED))
    }
}