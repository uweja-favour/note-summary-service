package com.xapps.note_summary.domain.repository

import com.xapps.model.TaskDraftStatus
import com.xapps.note_summary.domain.model.NoteSummaryDraft

interface NoteSummaryDraftRepository {

    suspend fun save(noteSummaryDraft: NoteSummaryDraft)

    suspend fun findByNoteSummaryId(noteSummaryId: String): NoteSummaryDraft?

    suspend fun deleteAllByDraftStatus(
        status: TaskDraftStatus
    ): Int

    suspend fun markCompleted(noteSummaryId: String)
    suspend fun markFailed(noteSummaryId: String)

}