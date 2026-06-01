package com.xapps.note_summary.infrastructure.messaging.kafka.producer

import com.xapps.messaging.kafka.KafkaTopics
import com.xapps.messaging.kafka.events.NoteSummaryRequestedEvent
import com.xapps.note_summary.application.generation.GenerateNoteSummaryCommand
import com.xapps.note_summary.application.port.out.PublishNoteSummaryGenerationRequestEventPort
import com.xapps.platform.core.compression.ObjectCompressionService
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaNoteSummaryGenerationRequestEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, ByteArray>,
    private val compressionService: ObjectCompressionService
) : PublishNoteSummaryGenerationRequestEventPort {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun publishNoteSummaryGenerationRequestEvent(command: GenerateNoteSummaryCommand) {
        log.info("Publishing note summary generation request...")

        val event = NoteSummaryRequestedEvent(
            noteSummaryId = command.noteSummaryId,
            userId = command.userId,
            jobId = command.jobId,
            fileKey = command.fileKey,
            style = command.style
        )

        val compressed: ByteArray = compressionService.compress(
            NoteSummaryRequestedEvent.serializer(),
            event
        )

        kafkaTemplate.send(
            KafkaTopics.NoteSummary.REQUESTED,
            command.jobId.value,
            compressed
        )
    }
}