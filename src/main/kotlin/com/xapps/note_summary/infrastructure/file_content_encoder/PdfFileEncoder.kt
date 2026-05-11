package com.xapps.note_summary.infrastructure.file_content_encoder

import com.github.uwejafavour.studentapplication__.core.common.file.model.EncodedFile
import com.xapps.model.FileType
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream

@Component
class PdfFileEncoder : FileContentEncoder {

    companion object {
        private const val MARGIN = 60f
        private const val FONT_SIZE = 11f
        private const val LEADING = 16f
        private const val PARAGRAPH_SPACING = 6f
        private val PAGE_SIZE = PDRectangle.A4
    }

    override fun supports(fileType: FileType): Boolean =
        fileType == FileType.PDF

    override fun encode(content: String, fileName: String): EncodedFile.Pdf {
        val document = PDDocument()

        val fontStream = javaClass.getResourceAsStream("/fonts/Inter_18pt-Regular.ttf")
            ?: error("Embedded font not found — ensure Inter-Regular.ttf is in src/main/resources/fonts/")
        val font = PDType0Font.load(document, fontStream)

        val usableWidth = PAGE_SIZE.width - 2 * MARGIN
        val startY = PAGE_SIZE.height - MARGIN
        val endY = MARGIN + FONT_SIZE

        val visualLines = resolveVisualLines(content, font, FONT_SIZE, usableWidth)

        var page = PDPage(PAGE_SIZE)
        document.addPage(page)
        var stream = PDPageContentStream(document, page)
        var y = startY

        for (line in visualLines) {
            if (y - LEADING < endY) {
                stream.close()
                page = PDPage(PAGE_SIZE)
                document.addPage(page)
                stream = PDPageContentStream(document, page)
                y = startY
            }

            if (line.isBlank()) {
                y -= PARAGRAPH_SPACING
                continue
            }

            stream.beginText()
            stream.setFont(font, FONT_SIZE)
            stream.newLineAtOffset(MARGIN, y)
            stream.showText(line)
            stream.endText()

            y -= LEADING
        }

        stream.close()

        val output = ByteArrayOutputStream()
        document.save(output)
        document.close()

        return EncodedFile.Pdf(output.toByteArray())
    }

    private fun resolveVisualLines(
        content: String,
        font: PDType0Font,
        fontSize: Float,
        maxWidth: Float
    ): List<String> {
        val result = mutableListOf<String>()
        for (paragraph in content.split("\n")) {
            if (paragraph.isBlank()) {
                result.add("")
                continue
            }
            result.addAll(wordWrap(paragraph, font, fontSize, maxWidth))
        }
        return result
    }

    private fun wordWrap(
        paragraph: String,
        font: PDType0Font,
        fontSize: Float,
        maxWidth: Float
    ): List<String> {
        val lines = mutableListOf<String>()
        val words = paragraph.split(" ")
        var currentLine = StringBuilder()

        for (word in words) {
            val candidate = if (currentLine.isEmpty()) word else "$currentLine $word"

            val width = try {
                textWidth(candidate, font, fontSize)
            } catch (e: Exception) {
                maxWidth + 1f
            }

            when {
                width <= maxWidth -> currentLine = StringBuilder(candidate)
                currentLine.isEmpty() -> lines.addAll(breakWordByChar(word, font, fontSize, maxWidth))
                else -> {
                    lines.add(currentLine.toString())
                    currentLine = StringBuilder(word)
                }
            }
        }

        if (currentLine.isNotEmpty()) lines.add(currentLine.toString())
        return lines
    }

    private fun breakWordByChar(
        word: String,
        font: PDType0Font,
        fontSize: Float,
        maxWidth: Float
    ): List<String> {
        val lines = mutableListOf<String>()
        var current = StringBuilder()
        for (char in word) {
            val candidate = "$current$char"
            val width = try { textWidth(candidate, font, fontSize) } catch (e: Exception) { maxWidth + 1f }
            if (width <= maxWidth) {
                current.append(char)
            } else {
                if (current.isNotEmpty()) lines.add(current.toString())
                current = StringBuilder(char.toString())
            }
        }
        if (current.isNotEmpty()) lines.add(current.toString())
        return lines
    }

    private fun textWidth(text: String, font: PDType0Font, fontSize: Float): Float =
        font.getStringWidth(text) / 1000f * fontSize
}