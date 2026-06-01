package com.xapps.note_summary.application.port.out

interface PublishNoteSummaryGeneratedEventPort {
    fun publishNoteSummaryGeneratedEvent(userId: String, ids: List<String>)
}