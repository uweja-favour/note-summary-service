package com.xapps.note_summary.persistence.repository.impl

import com.xapps.model.TaskDraftStatus
import com.xapps.note_summary.domain.model.NoteSummaryDraft
import com.xapps.note_summary.domain.repository.NoteSummaryDraftRepository
import com.xapps.note_summary.persistence.mapper.NoteSummaryDraftMapper
import com.xapps.note_summary.persistence.repository.NoteSummaryDraftMongoRepository
import com.xapps.note_summary.persistence.saveUpserting
import org.springframework.stereotype.Component

@Component
class NoteSummaryDraftRepositoryImpl(
    private val mongoRepository: NoteSummaryDraftMongoRepository,
    private val mapper: NoteSummaryDraftMapper
) : NoteSummaryDraftRepository {

    override suspend fun save(noteSummaryDraft: NoteSummaryDraft) {
        mongoRepository.saveUpserting(mapper.toDocument(noteSummaryDraft))
    }

    override suspend fun findByNoteSummaryId(noteSummaryId: String): NoteSummaryDraft? {
        return mongoRepository.findByNoteSummaryId(noteSummaryId)?.let {
            mapper.toDomain(it)
        }
    }

    override suspend fun deleteAllByDraftStatus(status: TaskDraftStatus): Int {
        return mongoRepository.deleteAllByDraftStatusCode(status.code)
    }

    override suspend fun markCompleted(noteSummaryId: String) {
        findByNoteSummaryId(noteSummaryId)?.let {
            save(it.copy(status = TaskDraftStatus.COMPLETED))
        }
    }

    override suspend fun markFailed(noteSummaryId: String) {
        findByNoteSummaryId(noteSummaryId)?.let {
            save(it.copy(status = TaskDraftStatus.FAILED))
        }
    }
}