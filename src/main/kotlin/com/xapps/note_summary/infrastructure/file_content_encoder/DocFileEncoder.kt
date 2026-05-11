package com.xapps.note_summary.infrastructure.file_content_encoder

import com.github.uwejafavour.studentapplication__.core.common.file.model.EncodedFile
import com.xapps.model.FileType
import org.apache.poi.hwpf.HWPFDocument
import org.apache.poi.poifs.filesystem.POIFSFileSystem
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@Component
class DocFileEncoder : FileContentEncoder {

    override fun supports(fileType: FileType): Boolean =
        fileType == FileType.DOC

    override fun encode(content: String, fileName: String): EncodedFile.Doc {
        val emptyDocBytes = createEmptyDocWithText(content)
        return EncodedFile.Doc(emptyDocBytes)
    }

    private fun createEmptyDocWithText(content: String): ByteArray {
        val template = createMinimalDocTemplate()
        val inputStream = ByteArrayInputStream(template)
        val fs = POIFSFileSystem(inputStream)
        val document = HWPFDocument(fs)
        val range = document.range

        // Insert each line as a separate paragraph to preserve line breaks
        content.split("\n").forEach { line ->
            range.insertAfter(line)
            range.insertAfter("\r") // .doc paragraph break
        }

        val out = ByteArrayOutputStream()
        document.write(out)
        document.close()

        return out.toByteArray()
    }

    private fun createMinimalDocTemplate(): ByteArray {
        val fs = POIFSFileSystem()
        val out = ByteArrayOutputStream()
        fs.writeFilesystem(out)
        fs.close()
        return out.toByteArray()
    }
}