package com.xapps.note_summary.infrastructure.object_store

import java.time.Instant

data class ObjectMetadata(
    val key: ObjectKey,
    val originalFilename: String,
    val contentType: String,
    val sizeBytes: Long,
    val uploadedAt: Instant,
    val storagePath: String
)