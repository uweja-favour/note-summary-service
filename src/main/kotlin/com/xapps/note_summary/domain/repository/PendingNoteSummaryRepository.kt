package com.xapps.note_summary.domain.repository

import com.xapps.model.DeliveryStatus
import com.xapps.note_summary.domain.model.PendingNoteSummary
import kotlinx.coroutines.flow.Flow

interface PendingNoteSummaryRepository {

    fun findAllByUserIdAndStatus(
        userId: String,
        status: DeliveryStatus = DeliveryStatus.PENDING
    ): Flow<PendingNoteSummary>

    suspend fun findByUserIdAndNoteSummaryId(
        userId: String,
        noteSummaryId: String
    ): PendingNoteSummary?

    suspend fun findByNoteSummaryId(noteSummaryId: String): PendingNoteSummary?
    suspend fun save(userPendingNoteSummary: PendingNoteSummary)
    suspend fun saveAll(userPendingNoteSummaries: List<PendingNoteSummary>)
}