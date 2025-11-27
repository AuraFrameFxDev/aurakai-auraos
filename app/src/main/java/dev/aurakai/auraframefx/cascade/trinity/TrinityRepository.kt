package dev.aurakai.auraframefx.cascade.trinity

import dev.aurakai.auraframefx.api.client.models.AgentStatus
import dev.aurakai.auraframefx.models.AgentRequest
import dev.aurakai.auraframefx.models.AgentResponse
import dev.aurakai.auraframefx.models.UserData
import dev.aurakai.auraframefx.network.AuraApiServiceWrapper
import dev.aurakai.auraframefx.network.model.Theme
import dev.aurakai.auraframefx.network.model.User
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrinityRepository @Inject constructor(
    private val apiService: AuraApiServiceWrapper,
) {

    // User related operations
    suspend fun getCurrentUser() = flow<Result<UserData>> {
        try {
            val response = apiService.userApi.getCurrentUser()
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    // AI Agent operations
    suspend fun getAgentStatus(agentType: String) = flow<Result<AgentStatus>> {
        try {
            val response = apiService.aiAgentApi.getAgentStatus(agentType)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun processAgentRequest(agentType: String, request: AgentRequest) = flow<Result<AgentResponse>> {
        try {
            val response = apiService.aiAgentApi.processRequest(agentType, request)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    // Theme operations
    suspend fun getThemes() = flow<Result<List<Theme>>> {
        try {
            val response = apiService.themeApi.getThemes()
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun applyTheme(themeId: String) = flow<Result<Theme>> {
        try {
            val response = apiService.themeApi.applyTheme(themeId)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    // Add more repository methods as needed for other API endpoints
}
