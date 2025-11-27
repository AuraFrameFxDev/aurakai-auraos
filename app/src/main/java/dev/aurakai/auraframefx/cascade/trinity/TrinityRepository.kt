package dev.aurakai.auraframefx.cascade.trinity

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import dev.aurakai.auraframefx.models.AgentRequest
import dev.aurakai.auraframefx.models.AgentResponse
import dev.aurakai.auraframefx.models.Theme
import dev.aurakai.auraframefx.models.UserData
import dev.aurakai.auraframefx.network.AuraApiServiceWrapper
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success
import dev.aurakai.auraframefx.models.AgentStatus as DomainAgentStatus
import dev.aurakai.auraframefx.network.model.Theme as NetworkTheme
import dev.aurakai.auraframefx.network.model.User as NetworkUser

private val currentTask: Any
    get() {
        TODO()
    }
private val visionState: String
    get() {
        TODO()
    }

@Singleton
class TrinityRepository @Inject constructor(
    private val apiService: AuraApiServiceWrapper,
    val it: NetworkTheme,
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
            emit(success(response))
        } catch (e: Exception) {
            emit(failure(e))
        }
    }


    private fun mapToUserData(networkUser: NetworkUser): UserData =
        UserData(
            id = networkUser.id,
            username = networkUser.username,
            email = networkUser.email,
            role = "user"
        )

    private fun mapToDomainAgentStatus(src: AgentResponse): DomainAgentStatus {
        val statusEnum = try {
            DomainAgentStatus.Status.valueOf(uppercase())
        } catch (_: Exception) {
            DomainAgentStatus.Status.IDLE
        }

        return DomainAgentStatus(src.agentType.toString(), statusEnum, try {
            toEpochMilli()
            } catch (_: Exception) {
                System.currentTimeMillis()
            }, metadata = mapOf(
                "visionState" to visionState,
                "currentTask" to currentTask
            )
        )
    }

    private fun DomainAgentStatus(
        agentId: String,
        status: DomainAgentStatus.Status,
        lastActiveTimestamp: Long,
        metadata: Map<String, Any>
    ): DomainAgentStatus {
        TODO("Not yet implemented")
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
        try { Color(hex.toColorInt()) } catch (_: Exception) { Color.Black }
}

private fun toEpochMilli(): Long {
    TODO("Not yet implemented")
}

private fun toInstant() {
    TODO("Not yet implemented")
}

private fun uppercase(): String {
    TODO("Not yet implemented")
}
