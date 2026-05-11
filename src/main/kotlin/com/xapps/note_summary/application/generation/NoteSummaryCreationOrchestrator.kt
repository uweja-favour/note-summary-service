package com.xapps.note_summary.application.generation

import com.xapps.note_summary.api.dto.CreateNoteSummaryRequest
import org.springframework.stereotype.Component

@Component
class NoteSummaryCreationOrchestrator(
    private val draftService: NoteSummaryDraftService,
    private val jobDispatcher: NoteSummaryJobDispatcher
) {

    suspend fun create(
        userId: String,
        request: CreateNoteSummaryRequest
    ) {
        val draft = draftService.createDraft(
            userId = userId,
            request = request
        )

        jobDispatcher.dispatch(
            GenerateNoteSummaryCommand(
                noteSummaryId = draft.noteSummaryId,
                fileKey = draft.fileKey,
                style = draft.style,
                userId = draft.userId,
                fileName = draft.fileName,
                fileType = draft.fileType
            )
        )
    }
}