package com.xapps.note_summary.domain.model

import com.xapps.model.FileData
import com.xapps.time.types.KotlinInstant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class NoteSummary(
    val id: String,
    val note: FileData,
    val style: NoteSummaryStyle,
    @Contextual val createdAt: KotlinInstant
)
