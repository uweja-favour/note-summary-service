package com.xapps.note_summary.persistence.entity

import com.xapps.model.TaskDraftStatusCode
import com.xapps.note_summary.domain.model.NoteSummaryStyleCode
import com.xapps.note_summary.persistence.BasePersistableEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("note_summary_draft")
data class NoteSummaryDraftDocument(
    @Id
    val id1: String,

    val noteSummaryId: String,
    val fileName: String,
    val fileMime: String,
    val styleCode: NoteSummaryStyleCode,
    val userId: String,
    val fileKey: String,
    val draftStatusCode: TaskDraftStatusCode
) : BasePersistableEntity() {

    override fun getTheId(): String =
        id1
}
