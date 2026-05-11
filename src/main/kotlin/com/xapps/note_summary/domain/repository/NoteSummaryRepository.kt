package com.xapps.note_summary.domain.repository

import com.xapps.note_summary.domain.model.NoteSummary

interface NoteSummaryRepository {
    suspend fun save(noteSummary: NoteSummary): NoteSummary
    suspend fun saveAll(noteSummaries: List<NoteSummary>): List<NoteSummary>
    suspend fun findById(noteSummaryId: String): NoteSummary?
    suspend fun findAll(): List<NoteSummary>
    suspend fun deleteById(id: String)
    suspend fun delete(noteSummary: NoteSummary)
    suspend fun deleteAll()
}