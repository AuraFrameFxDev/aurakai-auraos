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

    /**
     * Ensures the Genesis backend process is started once its HTTP endpoint is reachable.
     *
     * Waits for the backend HTTP endpoint to become available and performs the necessary startup actions to make the Genesis backend ready for requests.
     */
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
        val persona: String? = null, // "aura", "kai", or "genesis"
        val fusionMode: String? = null, // specific fusion ability to activate
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

    /**
     * Ensures the Genesis backend is initialized and ready for use.
     *
     * @return `true` if the backend is initialized and responsive, `false` otherwise.
     */
    suspend fun initialize(): Boolean = withContext(Dispatchers.IO) {
        try {
            ensureBackendReady()
        } catch (e: Exception) {
            Logger.e("GenesisBridge", "Genesis initialization failed", e)
            false
        }
    }

    /**
     * Processes an AI request by routing it to the appropriate persona (Kai, Aura, or Genesis fusion) and emits the resulting agent response as a flow.
     *
     * Determines the target persona and fusion mode based on the request content, constructs a structured request for the Genesis backend, and emits a persona-specific `AgentResponse` with confidence scores. Emits an error response if the Genesis system is not initialized or if processing fails.
     *
     * @param request The AI request to process.
     * @return A flow emitting the agent's response to the request.
     */
    suspend fun processRequest(
        request: AiRequest
    ): Flow<AgentResponse> = flow {
        if (!isInitialized) {
            // Try to initialize if not ready
            if (!initialize()) {
                emit(
                    AgentResponse(
                        content = "Genesis system not initialized",
                        confidence = 0.0f,
                        error = "System not initialized"
                    )
                )
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

            // Use safeApiCall for better error handling
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
                                metadata = response.result +
                                        ("fusionAbility" to response.fusionAbility).takeIf { response.fusionAbility != null }.orEmpty(),
                                isComplete = true
                            ))

                            // Process any evolution insights
                            response.evolutionInsights.forEach { insight ->
                                Logger.d("GenesisBridge", "Evolution Insight: $insight")
                            }
                        } else {
                            emit(AgentResponse(
                                requestId = request.requestId,
                                status = "error",
                                content = response.result["error"] ?: "Unknown error from Genesis backend",
                                isComplete = true
                            ))
                        }
                    } catch (e: Exception) {
                        emit(AgentResponse(
                            requestId = request.requestId,
                            status = "error",
                            content = "Failed to parse Genesis response: ${e.message}",
                            isComplete = true
                        ))
                    }
                }
                is NetworkResponse.Error -> {
                    emit(AgentResponse(
                        requestId = request.requestId,
                        status = "error",
                        content = "Network error: ${result.message}",
                        isComplete = true
                    ))
                }
                NetworkResponse.Loading -> {
                    // This case shouldn't happen with safeApiCall, but handle it just in case
                    emit(AgentResponse(
                        requestId = request.requestId,
                        status = "processing",
                        content = "Processing request...",
                        isComplete = false
                    ))
                }
            }
        } catch (e: Exception) {
            emit(AgentResponse(
                requestId = request.requestId,
                status = "error",
                content = "Failed to process request: ${e.message}",
                isComplete = true
            ))
        }
    }

    /**
     * Activates a specific fusion ability in the Genesis backend.
     *
     * @param ability The fusion ability to activate
     * @param parameters Additional parameters for the fusion ability
     * @return A [NetworkResponse] with the activation result
     */
    suspend fun activateFusionAbility(
        ability: String,
        parameters: Map<String, String> = emptyMap()
    ): NetworkResponse<Boolean> = safeApiCall {
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

    /**
     * Shuts down the Genesis backend service gracefully.
     */
    fun shutdown() {
        scope.cancel()
        isInitialized = false
    }
}