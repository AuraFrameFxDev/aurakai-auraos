package dev.aurakai.auraframefx.cascade.trinity

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import dev.aurakai.auraframefx.models.AgentRequest
import dev.aurakai.auraframefx.models.AgentResponse
import dev.aurakai.auraframefx.models.Theme
import dev.aurakai.auraframefx.models.UserData
import dev.aurakai.auraframefx.network.AuraApiServiceWrapper
import dev.aurakai.auraframefx.network.model.*
import dev.aurakai.auraframefx.models.*
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success
import dev.aurakai.auraframefx.models.AgentStatus as DomainAgentStatus
import dev.aurakai.auraframefx.network.model.Theme as NetworkTheme
import dev.aurakai.auraframefx.network.model.User as NetworkUser

@Singleton
class TrinityRepository @Inject constructor(
    private val apiService: AuraApiServiceWrapper
) {
    // User related operations
    fun getCurrentUser() = flow {
        try {
            val response = apiService.userApi.getCurrentUser()
            emit(success(mapToUserData(response)))
        } catch (e: Exception) {
            emit(failure(e))
        }
    }

    // AI Agent operations
    fun getAgentStatus(agentType: String) = flow {
        try {
            val response = apiService.aiAgentApi.getAgentStatus(agentType)
            emit(success(mapToDomainAgentStatus(response)))
        } catch (e: Exception) {
            emit(failure(e))
        }
    }

    fun processAgentRequest(agentType: String, request: AgentRequest) = flow {
        try {
            val response = apiService.aiAgentApi.processRequest(agentType, request)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    // Theme operations
    fun getThemes() = flow<Result<List<Theme>>> {
        try {
            val response = apiService.themeApi.getThemes()
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun applyTheme(themeId: String) = flow<Result<Theme>> {
        try {
            val response = apiService.themeApi.applyTheme(themeId)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    // Mapper functions
    private fun mapToUserData(networkUser: NetworkUser): UserData {
        return UserData(
            id = networkUser.id,
            name = networkUser.name,
            email = networkUser.email,
            profileImageUrl = networkUser.profileImageUrl
        )
    }

    private fun mapToDomainAgentStatus(networkStatus: dev.aurakai.auraframefx.network.model.AgentStatus): DomainAgentStatus {
        return DomainAgentStatus(
            agentType = networkStatus.agentType,
            isActive = networkStatus.isActive,
            lastActivity = networkStatus.lastActivity,
            capabilities = networkStatus.capabilities
        )
    }

    // Add more repository methods as needed for other API endpoints
}
