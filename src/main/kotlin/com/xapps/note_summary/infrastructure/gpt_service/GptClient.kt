package com.xapps.note_summary.infrastructure.gpt_service

interface GptClient {

    /**
     * Sends a prompt to GPT and expects a plain string response.
     */
    suspend fun generateStringResponse(prompt: String, allowGpt5: Boolean = false): String
}