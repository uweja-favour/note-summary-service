package com.xapps.note_summary.infrastructure.messaging.kafka.consumer

import com.xapps.messaging.kafka.KafkaGroupIds
import com.xapps.messaging.kafka.KafkaTopics
import com.xapps.messaging.kafka.events.NoteSummaryGeneratedEvent
import com.xapps.note_summary.application.generation.NoteSummaryGenerationCompletionHandler
import com.xapps.platform.core.compression.ObjectCompressionService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaNoteSummaryGeneratedConsumer(
    private val noteSummaryGenerationCompletionHandler: NoteSummaryGenerationCompletionHandler,
    private val compressionService: ObjectCompressionService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @KafkaListener(
        topics = [KafkaTopics.NoteSummary.GENERATED],
        groupId = KafkaGroupIds.NOTE_SUMMARY_SERVICE_GROUP
    )
    fun handle(payload: ByteArray) {

        log.info("NoteSummaryGeneratedEvent CONSUMED")

        val event = compressionService.decompress(
            NoteSummaryGeneratedEvent.serializer(),
            payload
        )

        CoroutineScope(Dispatchers.Default).launch {
            noteSummaryGenerationCompletionHandler.complete(
                noteSummaryId = event.noteSummaryId,
                userId = event.userId,
                content = event.content
            )
        }
    }
}