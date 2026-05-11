package com.xapps.note_summary.persistence.mapper

import com.xapps.note_summary.domain.model.NoteSummary
import com.xapps.note_summary.domain.model.NoteSummaryStyle
import com.xapps.note_summary.persistence.entity.NoteSummaryDocument
import org.springframework.stereotype.Component

@Component
class NoteSummaryMapper(
    private val fileDataMapper: FileDataMapper
) {

    fun toDomain(doc: NoteSummaryDocument): NoteSummary {
        return NoteSummary(
            id = doc.getTheId(),
            note = fileDataMapper.toDomain(doc.noteFile),
            style = requireNotNull(NoteSummaryStyle.fromCode(doc.styleCode)) {
                "style code is unknown: ${doc.styleCode}"
            },
            createdAt = doc.createdAt
        )
    }

    fun toDocument(domain: NoteSummary): NoteSummaryDocument {
        return NoteSummaryDocument(
            id1 = domain.id,
            noteFile = fileDataMapper.toDocument(domain.note),
            styleCode = domain.style.code,
            createdAt = domain.createdAt
        )
    }
}