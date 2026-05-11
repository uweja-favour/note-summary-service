package com.xapps.note_summary.persistence.repository.impl

import com.xapps.note_summary.domain.model.NoteSummary
import com.xapps.note_summary.domain.repository.NoteSummaryRepository
import com.xapps.note_summary.persistence.mapper.NoteSummaryMapper
import com.xapps.note_summary.persistence.repository.NoteSummaryMongoRepository
import com.xapps.note_summary.persistence.saveAllUpserting
import com.xapps.note_summary.persistence.saveUpserting
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Component

@Component
class NoteSummaryRepositoryImpl(
    private val mongoRepository: NoteSummaryMongoRepository,
    private val mapper: NoteSummaryMapper
) : NoteSummaryRepository {

    override suspend fun save(noteSummary: NoteSummary): NoteSummary {
        mongoRepository.saveUpserting(mapper.toDocument(noteSummary))
        return noteSummary
    }

    override suspend fun saveAll(noteSummaries: List<NoteSummary>): List<NoteSummary> {
        mongoRepository.saveAllUpserting(noteSummaries.map { mapper.toDocument(it) })
        return noteSummaries
    }

    override suspend fun findById(noteSummaryId: String): NoteSummary? {
        return mongoRepository.findById(noteSummaryId)?.let { mapper.toDomain(it) }
    }

    override suspend fun findAll(): List<NoteSummary> {
        return mongoRepository.findAll().toList().map { mapper.toDomain(it) }
    }

    override suspend fun deleteById(id: String) {
        mongoRepository.deleteById(id)
    }

    override suspend fun delete(noteSummary: NoteSummary) {
        deleteById(noteSummary.id)
    }

    override suspend fun deleteAll() {
        mongoRepository.deleteAll()
    }
}