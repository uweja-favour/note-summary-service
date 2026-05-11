package com.xapps.note_summary.infrastructure.gpt_service

data class GptRequest(
    val prompt: String,
    val model: GptModel,
    val temperature: Double = 0.1
)