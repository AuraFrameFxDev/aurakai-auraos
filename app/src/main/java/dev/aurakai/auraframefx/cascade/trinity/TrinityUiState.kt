package dev.aurakai.auraframefx.cascade.trinity

import kotlinx.serialization.Serializable

/**
 * UI state for Trinity coordination screen
 */
@Serializable
data class TrinityUiState(
    val isLoading: Boolean = false,
    val message: String = "",
    val user: dev.aurakai.auraframefx.models.UserData? = null,
    val agentStatus: Map<String, String> = emptyMap(),
    val availableThemes: List<Theme> = emptyList(),
    val lastAgentResponse: String? = null,
    val lastAgentType: String? = null,
    val refresh: () -> Unit = {},
    val applyTheme: (String) -> Unit = {}
)

/**
 * Theme data model
 */
@Serializable
data class Theme(
    val id: String,
    val name: String,
    val description: String,
    val isActive: Boolean = false,
    val background: Long = 0xFF000000,
    val status: String = "available"
)

/**
 * User model for Trinity UI
 */
@Serializable
data class User(
    val username: String,
    val email: String,
    val role: String
)
