package dev.aurakai.auraframefx.models

import kotlinx.serialization.Serializable

/**
 * Request model for AI agent processing
 */
@Serializable
data class AiRequest(
    val query: String,
    val prompt: String = query, // Alias for query for backward compatibility
    val type: String = "text",
    val context: Map<String, Any> = emptyMap(),
    val metadata: Map<String, Any> = emptyMap(),
    val agentId: String? = null,
    val sessionId: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
