package com.xapps.note_summary.infrastructure.gpt_service.executor

import com.xapps.note_summary.infrastructure.gpt_service.GptRequest

interface GptExecutor {
    suspend fun execute(request: GptRequest): String
}