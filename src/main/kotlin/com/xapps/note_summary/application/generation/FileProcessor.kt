package com.xapps.note_summary.application.generation

import com.xapps.dto.FileUploadDTO
import com.xapps.model.mime
import com.xapps.note_summary.infrastructure.object_store.ObjectHandle
import com.xapps.note_summary.infrastructure.object_store.ObjectKey
import com.xapps.note_summary.infrastructure.object_store.ObjectStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Component

@Component
class FileProcessor(
    private val objectStore: ObjectStore
) {

    private val semaphore = Semaphore(4)

    suspend fun upload(file: FileUploadDTO): ObjectKey =
        withContext(Dispatchers.IO) {
            uploadFiles(listOf(file)).first()
        }

    suspend fun uploadFiles(
        files: List<FileUploadDTO>
    ): List<ObjectKey> = coroutineScope {
        files.map { dto ->
            async {
                objectStore.put(
                    fileName = dto.fileName,
                    mimeType = dto.mime(),
                    bytes = dto.encodedFile.bytes
                ).key   // PutResult.key is the FileKey the consumer service will use to retrieve
            }
        }.awaitAll()
    }

    suspend fun fetch(key: ObjectKey): ObjectHandle {
        return fetchAll(listOf(key)).first()
    }

    suspend fun fetchAll(keys: List<ObjectKey>): List<ObjectHandle> = coroutineScope {
        keys.map { key ->
            semaphore.withPermit {
                async {
                    objectStore.get(key)
                }
            }
        }.awaitAll().mapNotNull { it }
    }

}