package com.xapps.note_summary.infrastructure.prompt_builder

import com.xapps.note_summary.domain.model.NoteSummaryStyle
import com.xapps.note_summary.infrastructure.prompt_builder.summary_instruction_policy.SummaryInstructionPolicyFactory
import org.springframework.stereotype.Component

@Component
class NoteSummaryPromptBuilderService(
    private val policyFactory: SummaryInstructionPolicyFactory,
    private val template: SummaryPromptTemplate
) {

    fun buildPrompt(
        note: String,
        style: NoteSummaryStyle
    ): String {
        require(note.isNotBlank()) { "Note cannot be empty" }

        val policy = policyFactory.create(style)
        val instructions = policy.instructions()

        return template.build(note, instructions)
    }
}