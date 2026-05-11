package com.xapps.note_summary.persistence.entity

import com.xapps.note_summary.domain.model.NoteSummaryStyleCode
import com.xapps.note_summary.persistence.BasePersistableEntity
import com.xapps.time.types.KotlinInstant
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("note_summary")
data class NoteSummaryDocument(
    @Id
    val id1: String,
    val noteFile: FileDataDocument,
    val styleCode: NoteSummaryStyleCode,
    val createdAt: KotlinInstant
) : BasePersistableEntity() {
    override fun getTheId(): String = id1
}