package com.xapps.note_summary.application.generation

import com.xapps.dto.job.JobDTO
import com.xapps.dto.job.JobStatus
import com.xapps.dto.job.JobTask
import com.xapps.note_summary.api.dto.CreateNoteSummaryRequest
import com.xapps.platform.core.string.generateUniqueId
import com.xapps.question_generation.JobId
import org.springframework.stereotype.Component

@Component
class NoteSummaryCreationOrchestrator(
    private val draftService: NoteSummaryDraftService,
    private val jobDispatcher: NoteSummaryJobDispatcher
) {

    suspend fun create(
        userId: String,
        request: CreateNoteSummaryRequest
    ): JobDTO {
        val draft = draftService.createDraft(
            userId = userId,
            request = request
        )

        val jobId = JobId.of(generateUniqueId())

        jobDispatcher.dispatch(
            GenerateNoteSummaryCommand(
                noteSummaryId = draft.noteSummaryId,
                jobId = jobId,
                fileKey = draft.fileKey,
                style = draft.style,
                userId = draft.userId,
                fileName = draft.fileName,
                fileType = draft.fileType
            )
        )

        return JobDTO(
            jobId = jobId,
            task = JobTask.NOTE_SUMMARY,
            status = JobStatus.Queued
        )
    }
}