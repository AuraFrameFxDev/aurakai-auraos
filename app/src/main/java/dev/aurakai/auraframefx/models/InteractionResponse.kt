package dev.aurakai.auraframefx.models

data class InteractionResponse(
    val content: String = "",
    val success: Boolean = true,
    val metadata: Map<String, Any> = emptyMap(),
    val timestamp: Long = System.currentTimeMillis()
)
