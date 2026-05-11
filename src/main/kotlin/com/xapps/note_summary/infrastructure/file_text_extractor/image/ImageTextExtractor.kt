package com.xapps.note_summary.infrastructure.file_text_extractor.image

import com.xapps.note_summary.infrastructure.file_text_extractor.ExtractedText
import com.xapps.note_summary.infrastructure.file_text_extractor.TextExtractor
import com.xapps.note_summary.infrastructure.file_text_extractor.ocr.OcrEngine
import com.xapps.platform.core.file.withTempFile
import org.springframework.stereotype.Component
import java.io.InputStream

@Component
class ImageTextExtractor(
    private val ocrEngine: OcrEngine
) : TextExtractor {

    private val supported = setOf(
        "jpg",
        "jpeg",
        "png",
        "bmp",
        "tiff",
        "webp"
    )

    override fun supports(extension: String): Boolean =
        extension in supported

    override suspend fun extract(input: InputStream, fileName: String): ExtractedText =
        withTempFile(".${fileName.substringAfterLast('.', "jpg")}") { temp ->
            temp.outputStream().use { input.copyTo(it) }
            ocrEngine.extractText(temp, fileName)
        }.let { ExtractedText.of(it, fileName) }
}
