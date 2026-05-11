package com.xapps.note_summary.infrastructure.gpt_service

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class HttpClientInitializer(
    @Value("\${openai.api.key}") private val openAiApiKey: String
) {
    @PostConstruct
    fun init() {
        HttpClientFactory.initialize(openAiApiKey)
    }
}