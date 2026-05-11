package com.xapps.note_summary.persistence.repository

import com.xapps.model.TaskDraftStatusCode
import com.xapps.note_summary.persistence.entity.NoteSummaryDraftDocument
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface NoteSummaryDraftMongoRepository : CoroutineCrudRepository<NoteSummaryDraftDocument, String> {
    suspend fun findByNoteSummaryId(noteSummaryId: String): NoteSummaryDraftDocument?

    suspend fun deleteAllByDraftStatusCode(
        draftStatusCode: TaskDraftStatusCode
    ): Int
}