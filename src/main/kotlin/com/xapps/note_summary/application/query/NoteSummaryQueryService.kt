package com.xapps.note_summary.application.query

import com.xapps.note_summary.domain.exception.NoteSummaryDomainError
import com.xapps.note_summary.domain.model.NoteSummary
import com.xapps.note_summary.domain.repository.NoteSummaryRepository
import org.springframework.stereotype.Component

@Component
class NoteSummaryQueryService(
    private val repository: NoteSummaryRepository
) {

    suspend fun getNoteSummary(
        noteSummaryId: String
    ): NoteSummary {

        val noteSummary = repository.findById(noteSummaryId)
            ?: throw NoteSummaryDomainError.NoteSummaryNotFound(noteSummaryId)

        return noteSummary
    }
}