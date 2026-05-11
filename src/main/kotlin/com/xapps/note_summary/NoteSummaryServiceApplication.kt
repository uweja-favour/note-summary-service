package com.xapps.note_summary

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
    exclude = [
        JacksonAutoConfiguration::class,
        org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration::class,
    ]
)
class NoteSummaryServiceApplication

fun main(args: Array<String>) {
    runApplication<NoteSummaryServiceApplication>(*args)
}

// RUN WITH DEV PROFILE
// ./gradlew :note-summary-service:bootRun --args='--spring.profiles.active=dev'


// ./gradlew :note-summary-service:bootRun


// A class may orchestrate many things or implement one thing, NEVER BOTH.
// Your class should either:
//
//Be a workflow conductor (this case), OR
//
//Be a rule executor