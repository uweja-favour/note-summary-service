package com.xapps.note_summary.persistence.mapper

import com.xapps.model.DeliveryStatus
import com.xapps.note_summary.domain.model.PendingNoteSummary
import com.xapps.note_summary.persistence.entity.PendingNoteSummaryDocument
import org.springframework.stereotype.Component

@Component
class PendingNoteSummaryMapper {

    fun toDomain(doc: PendingNoteSummaryDocument): PendingNoteSummary {
        return PendingNoteSummary(
            id = doc.getTheId(),
            noteSummaryId = doc.noteSummaryId,
            userId = doc.userId,
            status = DeliveryStatus.fromCode(doc.statusCode),
            createdAt = doc.createdAt
        )
    }

    fun toDocument(domain: PendingNoteSummary): PendingNoteSummaryDocument {
        return PendingNoteSummaryDocument(
            id1 = domain.id,
            noteSummaryId = domain.noteSummaryId,
            userId = domain.userId,
            statusCode = domain.status.code,
            createdAt = domain.createdAt
        )
    }
}