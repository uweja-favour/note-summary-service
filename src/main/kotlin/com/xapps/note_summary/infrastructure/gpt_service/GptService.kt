package com.xapps.note_summary.infrastructure.gpt_service

import com.xapps.note_summary.infrastructure.gpt_service.executor.GptExecutor
import kotlinx.serialization.json.JsonObject
import org.springframework.stereotype.Service

@Service
class GptService(
    private val executor: GptExecutor,
    private val modelSelector: GptModelSelector
) : GptClient {

    override suspend fun generateStringResponse(
        prompt: String,
        allowGpt5: Boolean
    ): String {

        val model = modelSelector.selectModel(prompt, allowGpt5)

        val request = GptRequest(
            prompt = prompt,
            model = model
        )

        val raw = executor.execute(request)
        return GptResponseParser.extractStringContent(raw)
    }
}