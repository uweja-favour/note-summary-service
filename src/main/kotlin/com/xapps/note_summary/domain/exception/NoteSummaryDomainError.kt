package com.xapps.note_summary.domain.exception

sealed class NoteSummaryDomainError : RuntimeException() {
    class NoteSummaryNotFound(val noteSummaryId: String) : NoteSummaryDomainError()
}