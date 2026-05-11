@file: OptIn(ExperimentalTime::class, ExperimentalSerializationApi::class)

package com.xapps.note_summary.infrastructure.serialization

import com.xapps.platform.serialization_core.BaseJsonEngine
import com.xapps.platform.serialization_core.KotlinInstantKxSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.modules.plus
import kotlin.time.ExperimentalTime

object NoteSummaryJsonEngine {

    val module =
        SerializersModule {

            // register other contextual serializers as needed via startup registration
        }

    val json: Json =
        Json {
            serializersModule = module + BaseJsonEngine.baseModule

            // LENIENT + RESILIENT settings
            ignoreUnknownKeys = true        // extra fields won't fail decoding
            isLenient = true                // accept slightly malformed JSON (e.g., single quotes)
            coerceInputValues = true        // missing/nullable values can be coerced rather than failing
            allowSpecialFloatingPointValues = true
            encodeDefaults = false
            prettyPrint = false
            explicitNulls = false
            classDiscriminator = "type"
        }

}
