package com.xapps.note_summary.persistence.entity

import com.xapps.model.DeliveryStatusCode
import com.xapps.note_summary.persistence.BasePersistableEntity
import com.xapps.time.types.KotlinInstant
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("user_pending_note_summary")
data class PendingNoteSummaryDocument(
    @Id
    val id1: String,

    val noteSummaryId: String,
    val userId: String,
    val statusCode: DeliveryStatusCode,
    val createdAt: KotlinInstant
): BasePersistableEntity() {
    override fun getTheId(): String = id1
}
