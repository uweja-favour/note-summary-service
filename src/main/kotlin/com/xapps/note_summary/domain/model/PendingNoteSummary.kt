package com.xapps.note_summary.domain.model

import com.xapps.model.DeliveryStatus
import com.xapps.time.types.KotlinInstant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

data class PendingNoteSummary(
    val id: String,

    val noteSummaryId: String,
    val userId: String,
    val status: DeliveryStatus,
    val createdAt: KotlinInstant
)