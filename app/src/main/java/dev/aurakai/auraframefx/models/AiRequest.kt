package dev.aurakai.auraframefx.models

import dev.aurakai.auraframefx.utils.MapStringAnySerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class AgentRequest(
    val type: String,
    val query: String? = null,
    @Serializable(with = MapStringAnySerializer::class)
    @Contextual
    val context: Map<String, Any>? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@Serializable
data class AiRequest @JvmOverloads constructor(
    val prompt: String,
    val type: String = "general",
    val query: String? = prompt,
    @Contextual
    val context: Map<String, Any>? = null,
    val timestamp: Long = System.currentTimeMillis()
)

annotation class AiRequestAnnotation
