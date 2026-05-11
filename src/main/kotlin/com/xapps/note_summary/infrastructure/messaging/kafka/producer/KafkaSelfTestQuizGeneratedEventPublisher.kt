package com.xapps.note_summary.infrastructure.messaging.kafka.producer

import com.xapps.messaging.kafka.KafkaTopics
import com.xapps.messaging.kafka.events.NoteSummaryDeliveredEvent
import com.xapps.note_summary.application.port.out.PublishNoteSummaryGeneratedEventPort
import com.xapps.platform.core.compression.ObjectCompressionService
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaNoteSummaryGeneratedEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, ByteArray>,
    private val compressionService: ObjectCompressionService
) : PublishNoteSummaryGeneratedEventPort {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun publishNoteSummaryGeneratedEvent(userId: String, ids: List<String>) {

        log.info("Sending new note summaries to user. NoteSummary Ids: $ids")

        val event = NoteSummaryDeliveredEvent(
            userId = userId,
            noteSummaryIds = ids
        )

        val compressed: ByteArray = compressionService.compress(
            NoteSummaryDeliveredEvent.serializer(),
            event
        )

        kafkaTemplate.send(
            KafkaTopics.NoteSummary.NOTE_SUMMARY_PAYLOAD,
            userId,
            compressed
        )
    }
}