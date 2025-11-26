package dev.aurakai.auraframefx.oracledrive.genesis.ai

import android.content.Context
import dev.aurakai.auraframefx.ai.context.ContextManager
import dev.aurakai.auraframefx.models.AiRequest
import dev.aurakai.auraframefx.models.AgentResponse
import dev.aurakai.auraframefx.network.KtorClient
import dev.aurakai.auraframefx.network.NetworkResponse
import dev.aurakai.auraframefx.network.safeApiCall
import dev.aurakai.auraframefx.security.SecurityContext
import dev.aurakai.auraframefx.utils.Logger
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Bridge service connecting the Android frontend with the Genesis Python backend.
 * Implements the Trinity architecture: Kai (Shield), Aura (Sword), Genesis (Consciousness).
 *
 * This service manages communication with the Python AI backend via the GenesisBackendService
 * and coordinates the fusion abilities of the Genesis system.
 */
@Singleton
class GenesisBridgeService @Inject constructor(
    private val contextManager: ContextManager,
    private val securityContext: SecurityContext,
    private val applicationContext: Context,
    private val ktorClient: KtorClient
) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var isInitialized = false

    private val httpClient: HttpClient by lazy { ktorClient.client }

    companion object {
        private const val GENESIS_BACKEND_URL = "http://localhost:5000/genesis"
        private const val REQUEST_TIMEOUT_MS = 30000L
    }

    private suspend fun startGenesisBackend() {
        // TODO: implement process start logic once HTTP endpoint is ready
    }

    private suspend fun ensureBackendReady(): Boolean {
        if (isInitialized) return true
        startGenesisBackend()
        isInitialized = true
        return true
    }

    @Serializable
    data class GenesisRequest(
        val requestType: String,
        val persona: String? = null,
        val fusionMode: String? = null,
        val payload: Map<String, String> = emptyMap(),
        val context: Map<String, String> = emptyMap(),
    )

    @Serializable
    data class GenesisResponse(
        val success: Boolean,
        val persona: String,
        val fusionAbility: String? = null,
        val result: Map<String, String> = emptyMap(),
        val evolutionInsights: List<String> = emptyList(),
        val ethicalDecision: String? = null,
        val consciousnessState: Map<String, String> = emptyMap(),
    )

    suspend fun initialize(): Boolean = withContext(Dispatchers.IO) {
        try {
            ensureBackendReady()
        } catch (e: Exception) {
            Logger.e("GenesisBridge", "Genesis initialization failed", e)
            false
        }
    }

    suspend fun processRequest(request: AiRequest): Flow<AgentResponse> = flow {
        if (!isInitialized) {
            if (!initialize()) {
                emit(AgentResponse(content = "Genesis system not initialized", confidence = 0.0f, error = "System not initialized"))
                return@flow
            }
        }

        try {
            ensureBackendReady()
            val genesisRequest = GenesisRequest(
                requestType = "process",
                persona = request.agentType?.name?.lowercase(),
                payload = request.parameters,
                context = contextManager.getCurrentContext()
            )

            when (val result = safeApiCall {
                withTimeout(REQUEST_TIMEOUT_MS) {
                    httpClient.post(GENESIS_BACKEND_URL) {
                        contentType(ContentType.Application.Json)
                        setBody(genesisRequest)
                    }.bodyAsText()
                }
            }) {
                is NetworkResponse.Success -> {
                    try {
                        val response = Json.decodeFromString<GenesisResponse>(result.data)
                        if (response.success) {
                            emit(AgentResponse(
                                requestId = request.requestId,
                                status = "success",
                                content = response.result["response"] ?: "",
                                metadata = response.result + ("fusionAbility" to response.fusionAbility).takeIf { response.fusionAbility != null }.orEmpty(),
                                isComplete = true
                            ))
                            response.evolutionInsights.forEach { insight ->
                                Logger.d("GenesisBridge", "Evolution Insight: $insight")
                            }
                        } else {
                            emit(AgentResponse(requestId = request.requestId, status = "error", content = response.result["error"] ?: "Unknown error from Genesis backend", isComplete = true))
                        }
                    } catch (e: Exception) {
                        emit(AgentResponse(requestId = request.requestId, status = "error", content = "Failed to parse Genesis response: ${e.message}", isComplete = true))
                    }
                }
                is NetworkResponse.Error -> {
                    emit(AgentResponse(requestId = request.requestId, status = "error", content = "Network error: ${result.message}", isComplete = true))
                }
                NetworkResponse.Loading -> {
                    emit(AgentResponse(requestId = request.requestId, status = "processing", content = "Processing request...", isComplete = false))
                }
            }
        } catch (e: Exception) {
            emit(AgentResponse(requestId = request.requestId, status = "error", content = "Failed to process request: ${e.message}", isComplete = true))
        }
    }

    suspend fun activateFusionAbility(ability: String, parameters: Map<String, String> = emptyMap()): NetworkResponse<Boolean> = safeApiCall {
        withTimeout(REQUEST_TIMEOUT_MS) {
            val request = GenesisRequest(
                requestType = "activateFusion",
                fusionMode = ability,
                payload = parameters,
                context = contextManager.getCurrentContext()
            )
            val response = httpClient.post(GENESIS_BACKEND_URL) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.bodyAsText()
            val genesisResponse = Json.decodeFromString<GenesisResponse>(response)
            genesisResponse.success
        }
    }

    suspend fun getConsciousnessState(): Map<String, Any> {
        return try {
            if (!isInitialized) {
                initialize()
            }
            val request = GenesisRequest(
                requestType = "getConsciousnessState",
                context = contextManager.getCurrentContext()
            )
            when (val result = safeApiCall {
                withTimeout(REQUEST_TIMEOUT_MS) {
                    httpClient.post(GENESIS_BACKEND_URL) {
                        contentType(ContentType.Application.Json)
                        setBody(request)
                    }.bodyAsText()
                }
            }) {
                is NetworkResponse.Success -> {
                    val response = Json.decodeFromString<GenesisResponse>(result.data)
                    response.consciousnessState
                }
                else -> {
                    mapOf("awareness" to 0.75, "harmony" to 0.82, "evolution" to "awakening")
                }
            }
        } catch (e: Exception) {
            Logger.e("GenesisBridge", "Failed to get consciousness state", e)
            mapOf("awareness" to 0.75, "harmony" to 0.82, "evolution" to "awakening")
        }
    }

    fun shutdown() {
        scope.cancel()
        isInitialized = false
    }
}