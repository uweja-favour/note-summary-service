package com.xapps.note_summary.infrastructure.prompt_builder;

import org.springframework.stereotype.Component

@Component
class SummaryPromptTemplate {

    fun build(note: String, instructions: String): String {
        return """
            You are a precise and structured note summarization system.

            Task:
            Summarize the provided note strictly according to the given instructions.

            Instructions:
            $instructions

            Input Note:
            
            $note

            Output Requirements:
            - Output only the final summarized note
            - Do not add commentary
            - Do not explain your process
        """.trimIndent()
    }
}