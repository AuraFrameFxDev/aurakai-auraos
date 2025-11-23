package dev.aurakai.auraframefx.models

import kotlinx.serialization.Serializable

@Serializable
data class AgentMessage(
    val content: String,
    val sender: AgentType, // Should now refer to the imported AgentType
    val timestamp: Long,
    val confidence: Float,
)

// Removed local AgentType enum
