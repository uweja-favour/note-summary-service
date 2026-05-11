package com.xapps.note_summary.infrastructure.file_content_encoder

import com.github.uwejafavour.studentapplication__.core.common.file.model.EncodedFile
import com.xapps.model.FileType

interface FileContentEncoder {
    fun supports(fileType: FileType): Boolean
    fun encode(content: String, fileName: String): EncodedFile
}