package com.xapps.note_summary.application.port.out

import com.xapps.note_summary.application.generation.GenerateNoteSummaryCommand

interface PublishNoteSummaryGenerationRequestEventPort {
    fun publishNoteSummaryGenerationRequestEvent(command: GenerateNoteSummaryCommand)
}