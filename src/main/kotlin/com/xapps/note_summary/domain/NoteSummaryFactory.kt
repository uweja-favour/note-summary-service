package com.xapps.note_summary.domain

import com.xapps.model.FileData
import com.xapps.model.FileType
import com.xapps.note_summary.domain.model.NoteSummary
import com.xapps.note_summary.domain.model.NoteSummaryStyle
import com.xapps.note_summary.infrastructure.file_content_encoder.FileContentEncoder
import com.xapps.platform.core.string.generateUniqueId
import com.xapps.time.types.KotlinInstant
import org.springframework.stereotype.Component
import kotlin.text.Charsets.UTF_8

@Component
class NoteSummaryFactory(
    private val fileEncoders: List<FileContentEncoder>
) {

    fun create(
        id: String,
        fileName: String,
        fileType: FileType,
        content: String,
        style: NoteSummaryStyle,
        now: KotlinInstant
    ): NoteSummary {
        val fileEncoder = fileEncoders.find { it.supports(fileType) }
            ?: error("Unsupported file type: $fileType")

        return NoteSummary(
            id = id,
            note = FileData(
                fileName = fileName,
                encodedFile = fileEncoder.encode(
                    content = content,
                    fileName = fileName
                )
            ),
            style = style,
            createdAt = now
        )
    }
}