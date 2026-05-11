package com.xapps.note_summary.infrastructure.file_content_encoder

import com.github.uwejafavour.studentapplication__.core.common.file.model.EncodedFile
import com.xapps.model.FileType
import org.apache.poi.xslf.usermodel.XMLSlideShow
import org.apache.poi.xslf.usermodel.XSLFTextBox
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream

@Component
class PptxFileEncoder : FileContentEncoder {

    override fun supports(fileType: FileType): Boolean =
        fileType == FileType.PPTX

    override fun encode(content: String, fileName: String): EncodedFile.Pptx {
        val ppt = XMLSlideShow()
        val slide = ppt.createSlide()

        val textBox: XSLFTextBox = slide.createTextBox()

        // Build one XSLFTextParagraph per line to preserve newlines
        content.split("\n").forEachIndexed { index, line ->
            val paragraph = if (index == 0) {
                textBox.textParagraphs[0] // reuse auto-created first paragraph
            } else {
                textBox.addNewTextParagraph()
            }
            val run = paragraph.addNewTextRun()
            run.setText(line)
        }

        val out = ByteArrayOutputStream()
        ppt.write(out)
        ppt.close()

        return EncodedFile.Pptx(out.toByteArray())
    }
}