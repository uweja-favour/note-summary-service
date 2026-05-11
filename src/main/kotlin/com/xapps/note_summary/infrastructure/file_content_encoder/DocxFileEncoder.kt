package com.xapps.note_summary.infrastructure.file_content_encoder

import com.github.uwejafavour.studentapplication__.core.common.file.model.EncodedFile
import com.xapps.model.FileType
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream

@Component
class DocxFileEncoder : FileContentEncoder {

    override fun supports(fileType: FileType): Boolean =
        fileType == FileType.DOCX

    override fun encode(content: String, fileName: String): EncodedFile.Docx {
        val doc = XWPFDocument()
        val paragraph = doc.createParagraph()
        val run = paragraph.createRun()

        content.split("\n").forEachIndexed { index, line ->
            if (index > 0) run.addBreak()
            run.setText(line, index)
        }

        val out = ByteArrayOutputStream()
        doc.write(out)
        doc.close()

        return EncodedFile.Docx(out.toByteArray())
    }
}