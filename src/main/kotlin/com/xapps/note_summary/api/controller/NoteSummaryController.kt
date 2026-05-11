package com.xapps.note_summary.api.controller

import com.xapps.dto.EmptyResponse
import com.xapps.dto.IdHolder
import com.xapps.note_summary.api.dto.CreateNoteSummaryRequest
import com.xapps.note_summary.application.ack.NoteSummaryAcknowledgementService
import com.xapps.note_summary.application.generation.NoteSummaryCreationOrchestrator
import com.xapps.note_summary.application.query.NoteSummaryQueryService
import com.xapps.note_summary.domain.model.NoteSummary
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/note_summary")
class NoteSummaryController(
    private val creation: NoteSummaryCreationOrchestrator,
    private val query: NoteSummaryQueryService,
    private val acknowledgment: NoteSummaryAcknowledgementService
) : ReactiveBaseController() {


    @PostMapping(
        "/create",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun createNoteSummary(
        @RequestBody request: CreateNoteSummaryRequest
    ): EmptyResponse =
        handle("createNoteSummary") {
            val principal = getAuthenticatedUserPrincipal()

            creation.create(
                userId = principal.userId,
                request = request
            )

            EmptyResponse()
        }

    @PostMapping(
        "/get",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun fetchNoteSummary(
        @RequestBody noteSummaryId: IdHolder
    ): NoteSummary =
        handle("getNoteSummary") {

            query.getNoteSummary(
                noteSummaryId = noteSummaryId.id
            )
        }


    @PostMapping(
        "/ack",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun ackNoteSummary(
        @RequestBody noteSummaryId: IdHolder
    ): EmptyResponse =
        handle("ackNoteSummary") {

            acknowledgment.acknowledgeNoteSummary(
                noteSummaryId = noteSummaryId.id
            )

            EmptyResponse()
        }
}