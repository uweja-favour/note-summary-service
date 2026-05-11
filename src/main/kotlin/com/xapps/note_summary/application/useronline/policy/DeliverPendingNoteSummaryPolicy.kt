package com.xapps.note_summary.application.useronline.policy

import com.xapps.model.DeliveryStatus
import com.xapps.note_summary.application.port.out.PublishNoteSummaryGeneratedEventPort
import com.xapps.note_summary.domain.repository.PendingNoteSummaryRepository
import kotlinx.coroutines.flow.toList
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class DeliverPendingNoteSummaryPolicy(
    private val pendingRepository: PendingNoteSummaryRepository,
    private val notificationPublisher: PublishNoteSummaryGeneratedEventPort
) : UserOnlinePolicy {

    private val log = LoggerFactory.getLogger(javaClass)

    override suspend fun execute(userId: String) {
        val noteSummaryIds = pendingRepository
            .findAllByUserIdAndStatus(userId, DeliveryStatus.PENDING)
            .toList()
            .map { it.noteSummaryId }

        if (noteSummaryIds.isEmpty()) return

        log.info("Discovered ${noteSummaryIds.count()} pending note summaries for user: $userId")

        notificationPublisher.publishNoteSummaryGeneratedEvent(userId, noteSummaryIds)
    }
}