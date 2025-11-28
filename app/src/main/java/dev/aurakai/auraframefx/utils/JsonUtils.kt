package dev.aurakai.auraframefx.utils

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import kotlinx.serialization.json.JsonPrimitive

/**
 * Utilities for converting Kotlin Maps to JsonObject for kotlinx.serialization compatibility.
 */

fun Map<String, *>?.toJsonObject(): JsonObject {
    val map = this ?: emptyMap<String, Any?>()
    val content = buildJsonObject {
        for ((k, v) in map) {
            when (v) {
                null -> put(k, JsonPrimitive(""))
                is String -> put(k, JsonPrimitive(v))
                is Number -> put(k, JsonPrimitive(v.toString()))
                is Boolean -> put(k, JsonPrimitive(v))
                is Map<*, *> -> {
                    // recursively convert nested maps (assuming keys are strings)
                    @Suppress("UNCHECKED_CAST")
                    put(k, (v as Map<String, *>).toJsonObject())
                }
                else -> put(k, JsonPrimitive(v.toString()))
            }
        }
    }
    return content
}

