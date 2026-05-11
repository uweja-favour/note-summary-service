package com.xapps.note_summary.infrastructure.object_store

data class PutResult(
    val key: ObjectKey,
    val metadata: ObjectMetadata
)