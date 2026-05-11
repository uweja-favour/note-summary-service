package com.xapps.note_summary.domain.model

import com.xapps.model.FileType
import com.xapps.model.TaskDraftStatus
import com.xapps.note_summary.infrastructure.object_store.ObjectKey

data class NoteSummaryDraft(
    val id: String,
    val noteSummaryId: String,
    val fileName: String,
    val fileType: FileType,
    val style: NoteSummaryStyle,
    val userId: String,
    val fileKey: ObjectKey,
    val status: TaskDraftStatus
)