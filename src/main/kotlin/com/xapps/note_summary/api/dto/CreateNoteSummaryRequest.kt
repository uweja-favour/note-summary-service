package com.xapps.note_summary.api.dto

import com.xapps.dto.FileUploadDTO
import com.xapps.note_summary.domain.model.NoteSummaryStyle
import kotlinx.serialization.Serializable

@Serializable
data class CreateNoteSummaryRequest(
    val file: FileUploadDTO,
    val style: NoteSummaryStyle
)
