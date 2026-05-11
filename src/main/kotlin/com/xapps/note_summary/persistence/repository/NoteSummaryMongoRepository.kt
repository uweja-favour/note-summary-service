package com.xapps.note_summary.persistence.repository

import com.xapps.note_summary.persistence.entity.NoteSummaryDocument
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface NoteSummaryMongoRepository : CoroutineCrudRepository<NoteSummaryDocument, String> {

}