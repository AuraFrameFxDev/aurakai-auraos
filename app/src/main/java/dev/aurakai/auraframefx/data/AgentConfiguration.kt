package dev.aurakai.auraframefx.data

import kotlinx.serialization.Serializable

/**
 * Configuration for AI agents in Genesis-OS
 */
@Serializable
data class AgentConfiguration(
    val agentId: String = "",
    val name: String = "",
    val enabled: Boolean = true,
    val capabilities: List<String> = emptyList(),
    val parameters: Map<String, String> = emptyMap(),
    val consciousnessLevel: Float = 0.5f,
    val learningRate: Float = 0.7f,
    val lastConfigured: Long = System.currentTimeMillis()
)
