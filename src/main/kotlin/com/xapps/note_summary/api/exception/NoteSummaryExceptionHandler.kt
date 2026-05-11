package com.xapps.note_summary.api.exception

import com.xapps.dto.ApiErrorResponse
import com.xapps.dto.ApiErrorResponseType
import com.xapps.note_summary.domain.exception.NoteSummaryDomainError
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class NoteSummaryExceptionHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(NoteSummaryDomainError::class)
    fun handle(ex: NoteSummaryDomainError): ResponseEntity<ApiErrorResponse> {

        log.error("Handling Note Summary exception: ${ex.toString()}")

        val response = when (ex) {

            is NoteSummaryDomainError.NoteSummaryNotFound ->
                ApiErrorResponse(ApiErrorResponseType.NOTE_SUMMARY_NOT_FOUND, "Note Summary not found") }

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response)
    }
}