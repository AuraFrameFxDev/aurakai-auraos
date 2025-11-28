package dev.aurakai.auraframefx.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class EnhancedInteractionData(
    val query: String = "",
    @Contextual val context: Map<String, Any> = emptyMap(),
    @Contextual val metadata: Map<String, Any> = emptyMap(),
    val timestamp: Long = System.currentTimeMillis()
)
