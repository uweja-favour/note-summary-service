package com.xapps.note_summary.infrastructure.gpt_service

import com.nimbusds.jose.shaded.gson.Gson
import kotlinx.serialization.json.*
import org.slf4j.LoggerFactory

/**
 * Responsible for extracting and cleaning GPT content from raw responses.
 */
object GptResponseParser {

    private val logger = LoggerFactory.getLogger(GptResponseParser.javaClass)

    private val gson = Gson()

    /**
     * Extracts raw string content from GPT response.
     */
    fun extractStringContent(responseText: String): String {
        val root = Json.parseToJsonElement(responseText).jsonObject

        root["error"]?.let { errorObj ->
            val message = errorObj.jsonObject["message"]?.jsonPrimitive?.content ?: "Unknown API error"
            throw RuntimeException("OpenAI API Error: $message")
        }

        val choice = root["choices"]?.jsonArray?.firstOrNull()?.jsonObject
            ?: throw RuntimeException("GPT response contains no choices")

        choice["finish_reason"]?.jsonPrimitive?.content
            ?.takeIf { it == "length" }
            ?.let { logger.warn("⚠️ GPT response was truncated due to length limit") }

        val content = choice["message"]?.jsonObject?.get("content")?.jsonPrimitive?.content
            ?: throw RuntimeException("GPT message content missing")

        return content.trim().removeSurrounding("```json").removeSurrounding("```").trim()
    }

    /**
     * Extracts content and parses it as JSON.
     */
    fun extractJsonContent(responseText: String): JsonObject {
        val content = extractStringContent(responseText)
        return runCatching { Json.parseToJsonElement(content).jsonObject }
            .getOrElse { throw RuntimeException("Failed to parse GPT response as JSON: ${it.message}") }
    }
}
