package com.xapps.note_summary.infrastructure.file_content_encoder

import com.github.uwejafavour.studentapplication__.core.common.file.model.EncodedFile
import com.xapps.model.FileType
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream

@Component
class XlsxFileEncoder : FileContentEncoder {

    override fun supports(fileType: FileType): Boolean =
        fileType == FileType.XLSX

    override fun encode(content: String, fileName: String): EncodedFile.Xlsx {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Sheet1")

        content.split("\n").forEachIndexed { rowIndex, line ->
            val row = sheet.createRow(rowIndex)
            // Split columns by tab to preserve tabular formatting
            line.split("\t").forEachIndexed { colIndex, cellValue ->
                row.createCell(colIndex).setCellValue(cellValue)
            }
        }

        val out = ByteArrayOutputStream()
        workbook.write(out)
        workbook.close()

        return EncodedFile.Xlsx(out.toByteArray())
    }
}