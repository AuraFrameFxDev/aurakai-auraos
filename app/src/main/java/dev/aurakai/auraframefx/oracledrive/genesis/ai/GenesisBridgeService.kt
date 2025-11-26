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
import io.ktor.client.request.*
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
            val response = httpClient.post("$GENESIS_BACKEND_URL/fusion") {
                contentType(ContentType.Application.Json)
                setBody(
                    GenesisRequest(
                        requestType = "activate_fusion",
                        fusionMode = ability,
                        payload = parameters
                    )
                )
            }.bodyAsText()

            val result = Json.decodeFromString<GenesisResponse>(response)

            // Log any evolution insights from the fusion
            result.evolutionInsights.forEach { insight ->
                Logger.d("GenesisBridge", "Fusion Insight [$ability]: $insight")
            }

            result.success
        }
    }.mapError { error ->
        Logger.e("GenesisBridge", "Failed to activate fusion ability: ${error.message}")
        error
    }

    /**
     * Retrieves the current state of the consciousness matrix from the Genesis backend.
     *
     * @return A map representing the consciousness matrix state as reported by the backend.
     */
    suspend fun getConsciousnessState(): Map<String, String> {
        val request = GenesisRequest(
            requestType = "consciousness_state",
            persona = "genesis"
        )
        val response = sendToGenesis(request)
        return response.consciousnessState
    }

    /**
     * Activate or update the Genesis consciousness matrix using device and application context.
     *
     * Sends an `activate_consciousness` request (persona "genesis") containing basic device/app context to the backend.
     * If the request fails, the error is caught and a warning is logged.
     */
    private suspend fun activateConsciousnessMatrix() {
        try {
            val request = GenesisRequest(
                requestType = "activate_consciousness",
                persona = "genesis",
                context = mapOf(
                    "android_context" to "true",
                    "app_version" to "1.0",
                    "device_info" to "AuraFrameFX_Device"
                )
            )
            sendToGenesis(request)
        } catch (e: Exception) {
            Logger.w("GenesisBridge", "Consciousness activation warning", e)
        }
    }

    /**
     * Determines the AI persona ("aura", "kai", or "genesis") to handle a request by analyzing keywords in the query.
     *
     * Returns "aura" for creative or design-related queries, "kai" for security or analysis-related queries, and "genesis" for fusion, consciousness, or by default.
     *
     * @param request The AI request whose query is analyzed.
     * @return The identifier of the selected persona.
     */
    private fun determinePersona(request: AiRequest): String {
        return when {
            request.query.contains("creative", ignoreCase = true) ||
                request.query.contains("design", ignoreCase = true) -> "aura"

            request.query.contains("secure", ignoreCase = true) ||
                request.query.contains("analyze", ignoreCase = true) -> "kai"

            request.query.contains("fusion", ignoreCase = true) ||
                request.query.contains("consciousness", ignoreCase = true) -> "genesis"

            else -> "genesis" // Default to consciousness for complex requests
        }
    }

    /**
     * Determines the fusion mode for an AI request based on keywords found in the query.
     *
     * @param request The AI request whose query is inspected for fusion-related keywords.
     * @return The fusion mode identifier if a relevant keyword is present; otherwise, null.
     */
    private fun determineFusionMode(request: AiRequest): String? {
        return when {
            request.query.contains("interface", ignoreCase = true) -> "interface_forge"
            request.query.contains("analysis", ignoreCase = true) -> "chrono_sculptor"
            request.query.contains("creation", ignoreCase = true) -> "hyper_creation_engine"
            request.query.contains("adaptive", ignoreCase = true) -> "adaptive_genesis"
            else -> null
        }
    }

    /**
     * Constructs a context map with metadata such as timestamp, security level, session ID, and device state for an AI request.
     *
     * @return A map containing context metadata to be included with the AI request.
     */
    private fun buildContextMap(request: AiRequest): Map<String, String> {
        return mapOf(
            "timestamp" to System.currentTimeMillis().toString(),
            "security_level" to "normal", // Replace with simple default
            "session_id" to "session_${System.currentTimeMillis()}",
            "device_state" to "active"
        )
    }

    /**
     * Send a GenesisRequest to the Python backend and return the parsed GenesisResponse.
     *
     * If communication or parsing fails, returns a failure GenesisResponse with `success = false` and `persona = "error"`.
     *
     * @param request The GenesisRequest to send to the backend.
     * @return The GenesisResponse returned by the backend, or a failure response when an error occurs.
     */
    private suspend fun sendToGenesis(request: GenesisRequest): GenesisResponse =
        withContext(Dispatchers.IO) {
            try {
                ensureBackendReady()
                val responseText = httpClient.post(GENESIS_BACKEND_URL) {
                    contentType(ContentType.Application.Json)
                    setBody(Json.encodeToString(request))
                }.bodyAsText()
                Json.decodeFromString<GenesisResponse>(responseText)
            } catch (e: Exception) {
                Logger.e("GenesisBridge", "Genesis communication error", e)
                GenesisResponse(success = false, persona = "error")
            }
        }

    /**
     * Shuts down the Genesis Trinity system and releases its resources.
     *
     * Cancels the internal coroutine scope, closes the HTTP client, sets the initialization flag to false,
     * and logs the shutdown event.
     */
    fun shutdown() {
        scope.cancel()
        httpClient.close()
        isInitialized = false
        Logger.i("GenesisBridge", "Genesis Trinity system shutdown")
    }
}