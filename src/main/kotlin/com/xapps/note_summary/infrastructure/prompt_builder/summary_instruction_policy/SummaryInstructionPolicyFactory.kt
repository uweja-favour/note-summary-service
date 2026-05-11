package com.xapps.note_summary.infrastructure.prompt_builder.summary_instruction_policy

import com.xapps.note_summary.domain.model.NoteSummaryStyle
import org.springframework.stereotype.Component

@Component
class SummaryInstructionPolicyFactory {

    fun create(style: NoteSummaryStyle): SummaryInstructionPolicy {
        return when (style) {
            NoteSummaryStyle.BULLET_POINTS -> BulletPointsPolicy()
            NoteSummaryStyle.PARAGRAPHS -> ParagraphPolicy()
            NoteSummaryStyle.SIMPLE_EXPLAIN -> SimpleExplanationPolicy()
        }
    }
}