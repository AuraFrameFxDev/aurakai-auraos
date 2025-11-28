package dev.aurakai.auraframefx.models

data class InteractionResponse(
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val metadata: Map<String, Any> = emptyMap()
)
