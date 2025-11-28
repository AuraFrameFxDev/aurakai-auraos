package dev.aurakai.auraframefx.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class InteractionResponse(
    val content: String = "",
    val success: Boolean = true,
    @Contextual val metadata: Map<String, Any> = emptyMap(),
    val timestamp: Long = System.currentTimeMillis()
)
