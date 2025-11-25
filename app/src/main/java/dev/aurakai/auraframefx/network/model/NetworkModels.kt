package dev.aurakai.auraframefx.network.model

import dev.aurakai.auraframefx.models.AgentResponse
import dev.aurakai.auraframefx.models.UserData
import kotlinx.serialization.Serializable

/**
 * Network models for API responses
 */

@Serializable
data class AgentRequest(
    val query: String,
    val context: Map<String, String> = emptyMap()
)

@Serializable
data class ThemeResponse(
    val themes: List<Theme>
)

import dev.aurakai.auraframefx.aura.themes.Theme

@Serializable
data class ApplyThemeResponse(
    val success: Boolean,
    val message: String
)

// API interfaces (stubs for now)
interface UserApi {
    suspend fun getCurrentUser(): UserData
}

interface AIAgentApi {
    suspend fun getAgentStatus(agentType: String): AgentResponse
    suspend fun processRequest(agentType: String, request: AgentRequest): AgentResponse
}

interface ThemeApi {
    suspend fun getThemes(): ThemeResponse
    suspend fun applyTheme(themeId: String): ApplyThemeResponse
}
