package com.xapps.note_summary.infrastructure.file_text_extractor.ocr

import com.xapps.note_summary.infrastructure.file_text_extractor.OcrFailure
import net.sourceforge.tess4j.Tesseract
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.File
import kotlin.getValue

@Component
class OcrEngine {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val tesseract: Tesseract by lazy {
        Tesseract().apply {
            val path = listOf(
                "/usr/share/tesseract-ocr/4.00/tessdata",
                "/usr/share/tesseract-ocr/tessdata",
                "/usr/local/share/tessdata",
                "/opt/homebrew/share/tessdata"
            ).firstOrNull { File(it).exists() }

            if (path == null) {
                logger.warn("No valid Tesseract path found")
            } else {
                setDatapath(path)
            }

            setLanguage("eng")
        }
    }

    fun extractText(image: File, fileName: String): String =
        tesseract.doOCR(image)
            .takeIf { it.isNotBlank() }
            ?: throw OcrFailure(fileName, "Tesseract")
}
