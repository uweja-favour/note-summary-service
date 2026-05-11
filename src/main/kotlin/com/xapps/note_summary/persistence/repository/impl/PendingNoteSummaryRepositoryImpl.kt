package com.xapps.note_summary.persistence.repository.impl

import com.xapps.model.DeliveryStatus
import com.xapps.note_summary.domain.model.PendingNoteSummary
import com.xapps.note_summary.domain.repository.PendingNoteSummaryRepository
import com.xapps.note_summary.persistence.mapper.PendingNoteSummaryMapper
import com.xapps.note_summary.persistence.repository.PendingNoteSummaryMongoRepository
import com.xapps.note_summary.persistence.saveAllUpserting
import com.xapps.note_summary.persistence.saveUpserting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Component

@Component
class PendingNoteSummaryRepositoryImpl(
    private val mongoRepository: PendingNoteSummaryMongoRepository,
    private val mapper: PendingNoteSummaryMapper
) : PendingNoteSummaryRepository {

    override fun findAllByUserIdAndStatus(
        userId: String,
        status: DeliveryStatus
    ): Flow<PendingNoteSummary> {
        return mongoRepository.findAllByUserIdAndStatusCode(userId = userId, statusCode = status.code)
            .map { mapper.toDomain(it) }
    }

    override suspend fun findByUserIdAndNoteSummaryId(
        userId: String,
        noteSummaryId: String
    ): PendingNoteSummary? {
        return mongoRepository.findByUserIdAndNoteSummaryId(
            userId = userId,
            noteSummaryId = noteSummaryId
        )?.let { mapper.toDomain(it) }
    }

    override suspend fun findByNoteSummaryId(noteSummaryId: String): PendingNoteSummary? {
        return mongoRepository.findByNoteSummaryId(noteSummaryId)
            ?.let { mapper.toDomain(it) }
    }

    override suspend fun save(userPendingNoteSummary: PendingNoteSummary) {
        mongoRepository.saveUpserting(mapper.toDocument(userPendingNoteSummary))
    }

    override suspend fun saveAll(userPendingNoteSummaries: List<PendingNoteSummary>) {
        mongoRepository.saveAllUpserting(userPendingNoteSummaries.map { mapper.toDocument(it) })
    }
}