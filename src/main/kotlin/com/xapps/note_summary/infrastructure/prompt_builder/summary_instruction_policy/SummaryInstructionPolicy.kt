package com.xapps.note_summary.infrastructure.prompt_builder.summary_instruction_policy

interface SummaryInstructionPolicy {
    fun instructions(): String
}