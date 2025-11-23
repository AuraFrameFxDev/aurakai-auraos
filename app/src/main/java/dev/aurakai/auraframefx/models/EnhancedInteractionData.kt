package dev.aurakai.auraframefx.models

data class EnhancedInteractionData(
    val query: String = "",
    val context: Map<String, Any> = emptyMap(),
    val metadata: Map<String, Any> = emptyMap(),
    val timestamp: Long = System.currentTimeMillis()
)
