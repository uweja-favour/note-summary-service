package com.xapps.note_summary.infrastructure.file_content_encoder

import com.github.uwejafavour.studentapplication__.core.common.file.model.EncodedFile
import com.xapps.model.FileType
import org.apache.poi.hslf.usermodel.HSLFSlide
import org.apache.poi.hslf.usermodel.HSLFSlideShow
import org.apache.poi.hslf.usermodel.HSLFTextBox
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream

@Component
class PptFileEncoder : FileContentEncoder {

    override fun supports(fileType: FileType): Boolean =
        fileType == FileType.PPT

    override fun encode(content: String, fileName: String): EncodedFile.Ppt {
        val ppt = HSLFSlideShow()
        val slide: HSLFSlide = ppt.createSlide()

        val textBox = HSLFTextBox()
        textBox.text = content  // HSLFTextBox preserves \n as paragraph breaks natively

        slide.addShape(textBox)

        val out = ByteArrayOutputStream()
        ppt.write(out)
        ppt.close()

        return EncodedFile.Ppt(out.toByteArray())
    }
}