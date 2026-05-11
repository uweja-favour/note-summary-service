package com.xapps.note_summary.persistence.mapper

import com.xapps.model.FileType
import com.xapps.model.TaskDraftStatus
import com.xapps.note_summary.domain.model.NoteSummaryDraft
import com.xapps.note_summary.domain.model.NoteSummaryStyle
import com.xapps.note_summary.persistence.entity.NoteSummaryDraftDocument
import org.springframework.stereotype.Component

@Component
class NoteSummaryDraftMapper {
    
    fun toDomain(doc: NoteSummaryDraftDocument): NoteSummaryDraft {
        return NoteSummaryDraft(
            id = doc.getTheId(),
            noteSummaryId = doc.noteSummaryId,
            fileName = doc.fileName,
            fileType = FileType.fileTypeOrNull(doc.fileMime) ?: error("Unknown file mime: ${doc.fileMime}"),
            style = NoteSummaryStyle.fromCode(doc.styleCode) ?: error("Unknown style code: ${doc.styleCode}"),
            userId = doc.userId,
            fileKey = doc.fileKey,
            status = TaskDraftStatus.fromCode(doc.draftStatusCode)
        )
    }
    
    fun toDocument(domain: NoteSummaryDraft): NoteSummaryDraftDocument {
        return NoteSummaryDraftDocument(
            id1 = domain.id,
            noteSummaryId = domain.noteSummaryId,
            fileName = domain.fileName,
            fileMime = domain.fileType.mime,
            styleCode = domain.style.code,
            userId = domain.userId,
            fileKey = domain.fileKey,
            draftStatusCode = domain.status.code
        )
    }
}