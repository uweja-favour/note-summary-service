package com.xapps.note_summary.infrastructure.file_content_encoder

import com.github.uwejafavour.studentapplication__.core.common.file.model.EncodedFile
import com.xapps.model.FileType
import org.springframework.stereotype.Component

@Component
class TextFileEncoder : FileContentEncoder {

    override fun supports(fileType: FileType): Boolean =
        fileType == FileType.TEXT

    override fun encode(
        content: String,
        fileName: String
    ): EncodedFile.Text {
        return EncodedFile.Text(content.toByteArray(Charsets.UTF_8))
    }
}