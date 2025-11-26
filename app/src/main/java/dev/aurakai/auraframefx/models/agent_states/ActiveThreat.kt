package dev.aurakai.auraframefx.models.agent_states

data class ActiveThreat(
    val id: String,
    val type: String,
    val severity: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis()
)
