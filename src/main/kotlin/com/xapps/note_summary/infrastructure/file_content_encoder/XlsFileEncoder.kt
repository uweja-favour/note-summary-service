package com.xapps.note_summary.infrastructure.file_content_encoder

import com.github.uwejafavour.studentapplication__.core.common.file.model.EncodedFile
import com.xapps.model.FileType
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream

@Component
class XlsFileEncoder : FileContentEncoder {

    override fun supports(fileType: FileType): Boolean =
        fileType == FileType.XLS

    override fun encode(content: String, fileName: String): EncodedFile.Xls {
        val workbook = HSSFWorkbook()
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

        return EncodedFile.Xls(out.toByteArray())
    }
}