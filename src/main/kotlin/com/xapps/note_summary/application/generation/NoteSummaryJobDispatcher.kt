package com.xapps.note_summary.application.generation

import com.xapps.note_summary.application.port.out.PublishNoteSummaryGenerationRequestEventPort
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class NoteSummaryJobDispatcher(
    private val generator: NoteSummaryGenerator,
    private val scope: CoroutineScope,
    private val generationRequestEventPort: PublishNoteSummaryGenerationRequestEventPort
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun dispatch(command: GenerateNoteSummaryCommand) {
        scope.launch {
//            outcomeOf {
//                generator.generate(command)
//            }.onFailure {
//                log.error("Failed to generate note summary for ${command.fileKey}")
//                 TODO: log + metrics
//            }
            generationRequestEventPort.publishNoteSummaryGenerationRequestEvent(command)
        }
    }
}