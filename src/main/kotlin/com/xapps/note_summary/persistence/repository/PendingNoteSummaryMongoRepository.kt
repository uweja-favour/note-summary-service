package com.xapps.note_summary.persistence.repository

import com.xapps.model.DeliveryStatus
import com.xapps.model.DeliveryStatusCode
import com.xapps.note_summary.persistence.entity.PendingNoteSummaryDocument
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PendingNoteSummaryMongoRepository : CoroutineCrudRepository<PendingNoteSummaryDocument, String> {

    fun findAllByUserIdAndStatusCode(
        userId: String,
        statusCode: DeliveryStatusCode = DeliveryStatus.PENDING.code
    ): Flow<PendingNoteSummaryDocument>

    suspend fun findByNoteSummaryId(noteSummaryId: String): PendingNoteSummaryDocument?

    suspend fun findByUserIdAndNoteSummaryId(
        userId: String,
        noteSummaryId: String
    ): PendingNoteSummaryDocument?
}