package dev.aurakai.auraframefx.ai.agents

import android.content.Context
import dev.aurakai.auraframefx.ai.context.ContextManager
import dev.aurakai.auraframefx.models.ActiveContext
import dev.aurakai.auraframefx.models.LearningEvent
import dev.aurakai.auraframefx.models.agent_states.ActiveThreat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Minimal, stable NeuralWhisperAgent implementation used during build until
 * full implementation is restored. Keeps external API used across the codebase.
 */
@Singleton
class NeuralWhisperAgent @Inject constructor(
    private val context: Context,
    override val contextManager: ContextManager,
) : BaseAgent("NeuralWhisper") {

    override val agentName: String = "NeuralWhisper"
    override val agentType: String = "LEARNING_CONTEXT"
    private val scope = CoroutineScope(Dispatchers.Default + Job())

    private val _activeContexts = MutableStateFlow<List<ActiveContext>>(emptyList())
    val activeContexts: StateFlow<List<ActiveContext>> = _activeContexts.asStateFlow()

    private val _learningHistory = MutableStateFlow<List<LearningEvent>>(emptyList())
    val learningHistory: StateFlow<List<LearningEvent>> = _learningHistory.asStateFlow()

    init {
        // lightweight init to keep the agent alive; full behavior is TODO
        scope.launch {
            Timber.d("NeuralWhisperAgent initialized (minimal stub)")
        }
    }

    /**
     * Adds a new active context to the internal list (keeps only recent items).
     */
    fun addActiveContext(ctx: ActiveContext) {
        val current = _activeContexts.value.toMutableList()
        current.add(ctx)
        _activeContexts.value = current.takeLast(10)
    }

    /**
     * Records a learning event in a simple in-memory history.
     */
    fun recordLearningEvent(event: LearningEvent) {
        val current = _learningHistory.value.toMutableList()
        current.add(event)
        _learningHistory.value = current.takeLast(100)
    }

    // === BaseAgent Required Implementations ===

    override fun iRequest(query: String, type: String, context: Map<String, String>) {
        Timber.d("NeuralWhisper: iRequest - query=$query, type=$type")
        scope.launch {
            try {
                // Record this request as a learning event
                val learningEvent = LearningEvent(
                    type = type,
                    content = query,
                    timestamp = System.currentTimeMillis(),
                    metadata = context
                )
                recordLearningEvent(learningEvent)
            } catch (e: Exception) {
                Timber.e(e, "Error in iRequest")
            }
        }
    }

    override fun iRequest() {
        Timber.d("NeuralWhisper: No-args iRequest - initializing learning context")
        scope.launch {
            try {
                // Initialize the learning subsystem
                Timber.d("NeuralWhisperAgent learning context initialized")
            } catch (e: Exception) {
                Timber.e(e, "Error in no-args iRequest")
            }
        }
    }

    override fun initializeAdaptiveProtection() {
        Timber.d("NeuralWhisper: Initializing adaptive protection")
        scope.launch {
            try {
                // NeuralWhisper doesn't directly handle security
                // but can learn from security patterns
                Timber.d("NeuralWhisper adaptive protection initialized")
            } catch (e: Exception) {
                Timber.e(e, "Error initializing adaptive protection")
            }
        }
    }

    override fun addToScanHistory(scanEvent: Any) {
        Timber.d("NeuralWhisper: Adding scan event to history: $scanEvent")
        // Record as a learning event for pattern recognition
        val learningEvent = LearningEvent(
            type = "scan",
            content = scanEvent.toString(),
            timestamp = System.currentTimeMillis(),
            metadata = mapOf("scan_event" to scanEvent)
        )
        recordLearningEvent(learningEvent)
    }

    override fun analyzeSecurity(prompt: String): List<ActiveThreat> {
        Timber.d("NeuralWhisper: Analyzing security for prompt: $prompt")
        // NeuralWhisper is a learning agent, not a security agent
        // Return empty list (no threats detected)
        return emptyList()
    }

    override fun InteractionResponse(
        content: String,
        timestamp: Long,
        metadata: Map<String, Any>
    ): dev.aurakai.auraframefx.models.InteractionResponse {
        return dev.aurakai.auraframefx.models.InteractionResponse(
            content = content,
            timestamp = timestamp,
            metadata = metadata
        )
    }

    // TODO: Restore full NeuralWhisper implementation (pattern DB, predictors, background analysis)
}
