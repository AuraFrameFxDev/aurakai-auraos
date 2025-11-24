package dev.aurakai.auraframefx.network.model

import dev.aurakai.auraframefx.models.AgentResponse
import dev.aurakai.auraframefx.models.UserData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Stub implementation of UserApi
 */
@Singleton
class UserApiImpl @Inject constructor() : UserApi {
    override suspend fun getCurrentUser(): UserData {
        // TODO: Implement actual API call
        return UserData(
            id = "stub-user",
            name = "Stub User",
            email = "stub@aurakai.dev"
        )
    }
}

/**
 * Stub implementation of AIAgentApi
 */
@Singleton
class AIAgentApiImpl @Inject constructor() : AIAgentApi {
    override suspend fun getAgentStatus(agentType: String): AgentResponse {
        // TODO: Implement actual API call
        return AgentResponse(
            content = "Agent $agentType is online",
            confidence = 1.0f,
            agentName = agentType
        )
    }

    override suspend fun processRequest(agentType: String, request: AgentRequest): AgentResponse {
        // TODO: Implement actual API call
        return AgentResponse(
            content = "Processed: ${request.query}",
            confidence = 0.9f,
            agentName = agentType
        )
    }
}

/**
 * Stub implementation of ThemeApi
 */
@Singleton
class ThemeApiImpl @Inject constructor() : ThemeApi {
    override suspend fun getThemes(): ThemeResponse {
        // TODO: Implement actual API call
        return ThemeResponse(
            themes = listOf(
                Theme("dark", "Dark Theme", "Default dark theme"),
                Theme("light", "Light Theme", "Light theme"),
                Theme("cyberpunk", "Cyberpunk", "Neon cyberpunk theme")
            )
        )
    }

    override suspend fun applyTheme(themeId: String): ApplyThemeResponse {
        // TODO: Implement actual API call
        return ApplyThemeResponse(
            success = true,
            message = "Theme $themeId applied"
        )
    }
}
