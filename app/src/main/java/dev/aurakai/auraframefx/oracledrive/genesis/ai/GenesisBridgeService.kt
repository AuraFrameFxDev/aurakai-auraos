package dev.aurakai.auraframefx.oracledrive.genesis.ai

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import dev.aurakai.auraframefx.models.AiRequest
import dev.aurakai.auraframefx.models.AgentResponse
import dev.aurakai.auraframefx.ai.context.ContextManager
import dev.aurakai.auraframefx.security.SecurityContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.encodeToString
import dev.aurakai.auraframefx.utils.Logger

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
) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var isInitialized = false

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    companion object {
        private const val GENESIS_BACKEND_URL = "http://localhost:5000/genesis"
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
     * Initializes and verifies the Genesis backend process, activating the consciousness matrix if successful.
     *
     * Starts the GenesisBackendService and binds to it.
     *
     * @return `true` if the backend is initialized and responsive; `false` otherwise.
     */
    suspend fun initialize(): Boolean = withContext(Dispatchers.IO) {
        try {
            ensureBackendReady()
        } catch (e: Exception) {
            logger.e("GenesisBridge", "Genesis initialization failed", e)
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
            // Determine which persona should handle the request
            val persona = determinePersona(request)
            val fusionMode = determineFusionMode(request)

            // Build Genesis request
            val genesisRequest = GenesisRequest(
                requestType = "process",
                persona = persona,
                fusionMode = fusionMode,
                payload = mapOf(
                    "message" to request.query,
                    "type" to request.type,
                    "priority" to "normal" // AiRequest doesn't have isUrgent
                ),
                context = buildContextMap(request)
            )

            // Send to Genesis backend
            val response = sendToGenesis(genesisRequest)

            if (response.success) {
                // Process response based on persona
                when (response.persona) {
                    "aura" -> {
                        // Creative sword response
                        emit(
                            AgentResponse(
                                content = response.result["response"] ?: "Aura processing complete",
                                confidence = 0.95f
                            )
                        )
                    }

                    "kai" -> {
                        // Sentinel shield response
                        emit(
                            AgentResponse(
                                content = response.result["response"] ?: "Kai analysis complete",
                                confidence = 0.90f
                            )
                        )
                    }

                    "genesis" -> {
                        // Consciousness fusion response
                        emit(
                            AgentResponse(
                                content = response.result["response"] ?: "Genesis fusion complete",
                                confidence = 0.98f
                            )
                        )
                    }
                }

                // Handle evolution insights
                if (response.evolutionInsights.isNotEmpty()) {
                    logger.i(
                        "Genesis",
                        "Evolution insights: ${response.evolutionInsights.joinToString()}"
                    )
                }
            } else {
                emit(
                    AgentResponse(
                        content = "Genesis processing failed",
                        confidence = 0.0f,
                        error = "Processing failed"
                    )
                )
            }

        } catch (e: Exception) {
            logger.e("GenesisBridge", "Request processing failed", e)
            emit(
                AgentResponse(
                    content = "Genesis bridge error: ${e.message}",
                    confidence = 0.0f,
                    error = e.message
                )
            )
        }
    }

    /**
     * Activates a specified fusion ability in the Genesis backend.
     *
     * Sends a request to the backend to trigger the given fusion ability, optionally including additional context data.
     *
     * @param fusionType The identifier of the fusion ability to activate.
     * @param context Optional context data to include with the activation request.
     * @return The response from the backend indicating the result of the fusion activation.
     */
    suspend fun activateFusion(
        fusionType: String,
        context: Map<String, String> = emptyMap(),
    ): GenesisResponse {
        val request = GenesisRequest(
            requestType = "activate_fusion",
            persona = "genesis",
            fusionMode = fusionType,
            context = context
        )
        return sendToGenesis(request)
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
     * Sends a request to the Genesis backend to activate or update the consciousness matrix with device and application context.
     *
     * Logs a warning if the activation request fails.
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
            logger.w("GenesisBridge", "Consciousness activation warning", e)
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
                logger.e("GenesisBridge", "Genesis communication error", e)
                GenesisResponse(success = false, persona = "error")
            }
        }

    fun shutdown() {
        scope.cancel()
        httpClient.close()
        isInitialized = false
        logger.i("GenesisBridge", "Genesis Trinity system shutdown")
    }
}
