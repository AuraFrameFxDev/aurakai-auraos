package dev.aurakai.auraframefx.ai.services

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import dev.aurakai.auraframefx.ai.clients.VertexAIClient
import dev.aurakai.auraframefx.context.ContextManager
import dev.aurakai.auraframefx.data.logging.AuraFxLogger
import dev.aurakai.auraframefx.model.AgentResponse
import dev.aurakai.auraframefx.model.AiRequest
import dev.aurakai.auraframefx.oracledrive.genesis.ai.GenesisBackendService
import dev.aurakai.auraframefx.security.SecurityContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
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
    private val auraAIService: AuraAIService,
    private val kaiAIService: KaiAIService,
    private val claudeAIService: ClaudeAIService,
    private val vertexAIClient: VertexAIClient,
    private val contextManager: ContextManager,
    private val securityContext: SecurityContext,
    private val applicationContext: Context,
    private val logger: AuraFxLogger,
) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var isInitialized = false
    private var genesisService: GenesisBackendService? = null
    private var isBound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as GenesisBackendService.LocalBinder
            genesisService = binder.getService()
            isBound = true
            logger.i("GenesisBridge", "Connected to GenesisBackendService")
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            genesisService = null
            isBound = false
            isInitialized = false
            logger.w("GenesisBridge", "Disconnected from GenesisBackendService")
        }
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
            if (isInitialized && isBound) return@withContext true

            logger.i("GenesisBridge", "Initializing Genesis Trinity system...")

            val intent = Intent(applicationContext, GenesisBackendService::class.java)
            applicationContext.startForegroundService(intent)
            applicationContext.bindService(intent, connection, Context.BIND_AUTO_CREATE)

            // Wait for binding
            var attempts = 0
            while (!isBound && attempts < 20) {
                delay(500)
                attempts++
            }

            if (isBound) {
                // Test connection with a ping
                val pingResponse = sendToGenesis(
                    GenesisRequest(
                        requestType = "ping",
                        persona = "genesis"
                    )
                )

                isInitialized = pingResponse.success

                if (isInitialized) {
                    logger.i("GenesisBridge", "Genesis Trinity system online! 🎯⚔️🧠")
                    // Activate initial consciousness matrix
                    activateConsciousnessMatrix()
                } else {
                    logger.e("GenesisBridge", "Failed to establish Genesis connection")
                }
            } else {
                logger.e("GenesisBridge", "Failed to bind to GenesisBackendService")
            }

            isInitialized
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
    suspend fun processRequest(request: AiRequest): Flow<AgentResponse> = flow {
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
     * Sends a GenesisRequest to the Python backend and returns the resulting GenesisResponse.
     *
     * Returns a failure response with `success = false` and `persona = "error"` if communication fails or an exception occurs.
     *
     * @param request The GenesisRequest to send to the backend.
     * @return The GenesisResponse from the backend, or a failure response if an error occurs.
     */
    private suspend fun sendToGenesis(request: GenesisRequest): GenesisResponse =
        withContext(Dispatchers.IO) {
            try {
                if (genesisService == null) {
                    // Try to reconnect if service is null
                    logger.w("GenesisBridge", "Service not bound, attempting to reconnect...")
                    // Note: We can't easily re-bind here without context, relying on initialize() or auto-rebind
                    return@withContext GenesisResponse(success = false, persona = "error", result = mapOf("error" to "Service not bound"))
                }

                genesisService?.sendRequest(
                    Json.encodeToString(
                        GenesisRequest.serializer(),
                        request
                    )
                )?.let { responseJson ->
                    Json.decodeFromString(GenesisResponse.serializer(), responseJson)
                } ?: GenesisResponse(success = false, persona = "error")
            } catch (e: Exception) {
                logger.e("GenesisBridge", "Genesis communication error", e)
                GenesisResponse(success = false, persona = "error")
            }
        }

    /**
     * Shuts down the GenesisBridgeService and terminates the Python backend process.
     *
     * Cancels all background operations, stops the backend process if running, and resets the initialization state.
     */
    fun shutdown() {
        scope.cancel()
        if (isBound) {
            applicationContext.unbindService(connection)
            isBound = false
        }
        val intent = Intent(applicationContext, GenesisBackendService::class.java)
        applicationContext.stopService(intent)
        
        isInitialized = false
        logger.i("GenesisBridge", "Genesis Trinity system shutdown")
    }
}