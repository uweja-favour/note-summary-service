package com.xapps.note_summary.infrastructure.messaging.kafka.consumer

import com.xapps.messaging.kafka.KafkaGroupIds
import com.xapps.messaging.kafka.KafkaTopics
import com.xapps.messaging.kafka.events.UserOnlineEvent
import com.xapps.note_summary.application.useronline.UserOnlineOrchestrator
import com.xapps.platform.core.compression.ObjectCompressionService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaUserOnlineConsumer(
    private val orchestrator: UserOnlineOrchestrator,
    private val compressionService: ObjectCompressionService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @KafkaListener(
        topics = [KafkaTopics.User.ONLINE],
        groupId = KafkaGroupIds.NOTE_SUMMARY_SERVICE_GROUP,
    )
    fun handle(payload: ByteArray) {

        log.info("Received user online event: $payload")

        val event = compressionService.decompress(
            UserOnlineEvent.serializer(),
            payload
        )

        CoroutineScope(Dispatchers.IO).launch {
            orchestrator.handleUserOnline(event.userId)
        }
    }
}