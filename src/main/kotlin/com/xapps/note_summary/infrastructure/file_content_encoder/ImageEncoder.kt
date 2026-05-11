package com.xapps.note_summary.infrastructure.file_content_encoder

import com.github.uwejafavour.studentapplication__.core.common.file.model.EncodedFile
import com.xapps.model.FileType
import org.springframework.stereotype.Component
import kotlin.io.encoding.Base64

@Component
class ImageEncoder : FileContentEncoder {

    override fun supports(fileType: FileType): Boolean =
        fileType in setOf(
            FileType.PNG,
            FileType.JPEG,
            FileType.GIF,
            FileType.BMP,
            FileType.WEBP
        )

    override fun encode(content: String, fileName: String): EncodedFile {

        val bytes = decodeBase64(content)

        return when {

            isJpeg(bytes) -> EncodedFile.Jpeg(bytes)

            isPng(bytes) -> EncodedFile.Png(bytes)

            isGif(bytes) -> EncodedFile.Gif(bytes)

            isBmp(bytes) -> EncodedFile.Bmp(bytes)

            isWebp(bytes) -> EncodedFile.Webp(bytes)

            else -> EncodedFile.Binary(bytes)
        }
    }

    private fun decodeBase64(content: String): ByteArray {
        return Base64.Default.decode(content)
    }

    private fun isJpeg(b: ByteArray) =
        b.size > 2 &&
                b[0] == 0xFF.toByte() &&
                b[1] == 0xD8.toByte()

    private fun isPng(b: ByteArray) =
        b.size > 4 &&
                b[0] == 0x89.toByte() &&
                b[1] == 0x50.toByte() &&
                b[2] == 0x4E.toByte() &&
                b[3] == 0x47.toByte()

    private fun isGif(b: ByteArray) =
        b.size > 3 &&
                b[0] == 'G'.code.toByte() &&
                b[1] == 'I'.code.toByte() &&
                b[2] == 'F'.code.toByte()

    private fun isBmp(b: ByteArray) =
        b.size > 2 &&
                b[0] == 'B'.code.toByte() &&
                b[1] == 'M'.code.toByte()

    private fun isWebp(b: ByteArray) =
        b.size > 12 &&
                b[0] == 'R'.code.toByte() &&
                b[1] == 'I'.code.toByte() &&
                b[2] == 'F'.code.toByte() &&
                b[8] == 'W'.code.toByte() &&
                b[9] == 'E'.code.toByte() &&
                b[10] == 'B'.code.toByte() &&
                b[11] == 'P'.code.toByte()
}