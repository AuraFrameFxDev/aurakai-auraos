package dev.aurakai.auraframefx.cascade.trinity

import dev.aurakai.auraframefx.network.AuraApiServiceWrapper
import dev.aurakai.auraframefx.network.model.Theme as NetworkTheme
import dev.aurakai.auraframefx.network.model.User as NetworkUser
import dev.aurakai.auraframefx.api.client.models.AgentStatus as NetworkAgentStatus
import dev.aurakai.auraframefx.models.AgentStatus as DomainAgentStatus
import dev.aurakai.auraframefx.models.AgentRequest
import dev.aurakai.auraframefx.models.AgentResponse
import dev.aurakai.auraframefx.models.Theme
import dev.aurakai.auraframefx.models.UserData
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.FlowCollector

@Singleton
class TrinityRepository @Inject constructor(
    private val apiService: AuraApiServiceWrapper,
) {
    // User related operations
    suspend fun getCurrentUser() = flow<Result<UserData>> {
        try {
            val response = apiService.userApi.getCurrentUser()
            emit(Result.success(mapToUserData(response)))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    // AI Agent operations
    suspend fun getAgentStatus(agentType: String) = flow<Result<DomainAgentStatus>> {
        try {
            val response: DomainAgentStatus = apiService.aiAgentApi.getAgentStatus(agentType)
            emit(Result.success(mapToDomainAgentStatus(response)))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    private fun FlowCollector<Result<DomainAgentStatus>>.mapToDomainAgentStatus(src: DomainAgentStatus): DomainAgentStatus {
        TODO("Not yet implemented")
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
            emit(Result.success(response.map { mapToDomainTheme(it) }))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun applyTheme(themeId: String) = flow<Result<Theme>> {
        try {
            val response = apiService.themeApi.applyTheme(themeId)
            emit(Result.success(mapToDomainTheme(response)))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    private fun mapToUserData(networkUser: NetworkUser): UserData =
        UserData(
            id = networkUser.id,
            username = networkUser.username,
            email = networkUser.email,
            role = "user"
        )

    private fun mapToDomainAgentStatus(src: NetworkAgentStatus): DomainAgentStatus {
        val statusEnum = try {
            DomainAgentStatus.Status.valueOf(src.processingState.uppercase())
        } catch (e: Exception) {
            DomainAgentStatus.Status.IDLE
        }

        return DomainAgentStatus(
            agentId = src.agentType.toString(),
            status = statusEnum,
            lastActiveTimestamp = try {
                src.lastActivity.toInstant().toEpochMilli()
            } catch (e: Exception) {
                System.currentTimeMillis()
            },
            metadata = mapOf(
                "visionState" to src.visionState,
                "currentTask" to (src.currentTask ?: "")
            )
        )
    }

    private fun mapToDomainTheme(networkTheme: NetworkTheme): Theme {
        val colors = networkTheme.colors
        return Theme(
            id = networkTheme.id,
            name = networkTheme.name,
            primaryColor = parseColor(colors?.primary ?: "#000000"),
            secondaryColor = parseColor(colors?.secondary ?: "#000000"),
            backgroundColor = parseColor(colors?.background ?: "#000000"),
            surfaceColor = parseColor(colors?.surface ?: "#000000"),
            onPrimaryColor = parseColor(colors?.onPrimary ?: "#FFFFFF"),
            onSecondaryColor = parseColor(colors?.onSecondary ?: "#FFFFFF"),
            onBackgroundColor = parseColor(colors?.onBackground ?: "#FFFFFF"),
            onSurfaceColor = parseColor(colors?.onSurface ?: "#FFFFFF"),
            isDark = (networkTheme.styles["mode"] == "dark")
        )
    }

    private fun parseColor(hex: String): Color =
        try { Color(android.graphics.Color.parseColor(hex)) } catch (_: Exception) { Color.Black }
}
