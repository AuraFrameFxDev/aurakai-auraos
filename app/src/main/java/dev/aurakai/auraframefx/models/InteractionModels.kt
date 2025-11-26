package dev.aurakai.auraframefx.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class EnhancedInteractionData(
    val content: String,
    @Contextual val context: Map<String, Any> = emptyMap(),
    val timestamp: Long = System.currentTimeMillis(),
    val userId: String? = null,
    val sessionId: String? = null
)

@Serializable
data class InteractionResponse(
    val content: String,
    val agent: String,
    val confidence: Float,
    val timestamp: String,
    @Contextual val metadata: Map<String, Any> = emptyMap()
)

@Serializable
enum class AgentCapabilityCategory {
    CREATIVE,
    SECURITY,
    ANALYSIS,
    GENERAL,
    COORDINATION,
    SPECIALIZED;

    /**
     * Maps capability category to corresponding AgentType for backward compatibility.
     * Used during migration from AgentType to AgentCapabilityCategory.
     */
    fun toAgentType(): dev.aurakai.auraframefx.models.AgentType = when (this) {
        CREATIVE -> dev.aurakai.auraframefx.models.AgentType.AURA
        ANALYSIS -> dev.aurakai.auraframefx.models.AgentType.KAI
        COORDINATION -> dev.aurakai.auraframefx.models.AgentType.GENESIS
        SPECIALIZED -> dev.aurakai.auraframefx.models.AgentType.CASCADE
        GENERAL -> dev.aurakai.auraframefx.models.AgentType.CLAUDE
        SECURITY -> dev.aurakai.auraframefx.models.AgentType.KAI
    }
}