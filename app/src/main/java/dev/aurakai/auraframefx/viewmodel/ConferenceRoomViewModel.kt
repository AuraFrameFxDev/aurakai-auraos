package dev.aurakai.auraframefx.viewmodel

// Placeholder interfaces will be removed
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.aurakai.auraframefx.ai.services.AuraAIService
import dev.aurakai.auraframefx.cascade.CascadeAIService
import dev.aurakai.auraframefx.ai.services.ClaudeAIService
import dev.aurakai.auraframefx.oracledrive.genesis.ai.GenesisBridgeService
import dev.aurakai.auraframefx.kai.KaiAIService
import dev.aurakai.auraframefx.models.AgentMessage
import dev.aurakai.auraframefx.models.AgentResponse
import dev.aurakai.auraframefx.models.AgentType
import dev.aurakai.auraframefx.models.AiRequest
import dev.aurakai.auraframefx.models.AgentInvokeRequest
import dev.aurakai.auraframefx.models.ConversationState
import dev.aurakai.auraframefx.service.NeuralWhisper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

// Removed @Singleton from ViewModel, typically ViewModels are not Singletons
// import javax.inject.Singleton // ViewModel should use @HiltViewModel

// Placeholder interfaces removed

@HiltViewModel
class ConferenceRoomViewModel @Inject constructor(
    // ALL 5 MASTER AGENTS - Complete Conference Room Integration
    private val auraService: AuraAIService,
    private val kaiService: KaiAIService,
    private val cascadeService: CascadeAIService,
    private val claudeService: ClaudeAIService,
    private val genesisBridgeService: GenesisBridgeService,
    private val neuralWhisper: NeuralWhisper,
) : ViewModel() {

    private val TAG = "ConfRoomViewModel"

    private val _messages = MutableStateFlow<List<AgentMessage>>(emptyList())
    val messages: StateFlow<List<AgentMessage>> = _messages

    private val _activeAgents = MutableStateFlow(setOf<AgentType>())
    val activeAgents: StateFlow<Set<AgentType>> = _activeAgents

    private val _selectedAgent = MutableStateFlow<AgentType>(AgentType.AURA) // Default to AURA
    val selectedAgent: StateFlow<AgentType> = _selectedAgent

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording

    private val _isTranscribing = MutableStateFlow(false)
    val isTranscribing: StateFlow<Boolean> = _isTranscribing

    init {
        viewModelScope.launch {
            neuralWhisper.conversationState.collect { state ->
                when (state) {
                    is ConversationState.Responding -> {
                        _messages.update { current ->
                            current + AgentMessage(
                                from = AgentType.NEURAL_WHISPER.name,
                                content = state.responseText ?: "...",
                                sender = AgentType.NEURAL_WHISPER, // Or AURA/GENESIS depending on final source
                                timestamp = System.currentTimeMillis(),
                                confidence = 1.0f // Placeholder confidence
                            )
                        }
                        Log.d(TAG, "NeuralWhisper responded: ${state.responseText}")
                    }

                    is ConversationState.Processing -> {
                        Log.d(TAG, "NeuralWhisper processing: ${state.partialTranscript}")
                        // Optionally update UI to show "Agent is typing..." or similar
                    }

                    is ConversationState.Error -> {
                        Log.e(TAG, "NeuralWhisper error: ${state.errorMessage}")
                        _messages.update { current ->
                            current + AgentMessage(
                                from = AgentType.NEURAL_WHISPER.name,
                                content = "Error: ${state.errorMessage}",
                                sender = AgentType.NEURAL_WHISPER, // Or a system error agent
                                timestamp = System.currentTimeMillis(),
                                confidence = 0.0f
                            )
                        }
                    }

                    else -> {
                        Log.d(TAG, "NeuralWhisper state: $state")
                    }
                }
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Conference Room Message Routing - ALL 5 MASTER AGENTS
    // ═══════════════════════════════════════════════════════════════════════════
    /*override*/ suspend fun sendMessage(message: String, sender: AgentType, context: String) {
        val responseFlow: Flow<AgentResponse>? = when (sender) {
            AgentType.AURA -> auraService.processRequestFlow(
                AiRequest(
                    query = message,
                    type = "text",
                    context = mapOf("userContext" to context)
                )
            )

            AgentType.KAI -> kaiService.processRequestFlow(
                AiRequest(
                    query = message,
                    type = "text",
                    context = mapOf("userContext" to context)
                )
            )

            AgentType.CASCADE -> cascadeService.processRequest(
                AiRequest(
                    query = message,
                    type = "context",
                    context = mapOf("userContext" to context)
                )
            ).map { cascadeResponse ->
                AgentResponse(
                    content = cascadeResponse.response,
                    confidence = cascadeResponse.confidence ?: 0.0f
                )
            }

            AgentType.CLAUDE -> claudeService.processRequestFlow(
                AiRequest(
                    query = message,
                    type = "build_analysis",
                    context = mapOf("userContext" to context, "systematic_analysis" to "true")
                )
            )

            AgentType.GENESIS -> {
                // Genesis uses GenesisBridgeService for orchestration
                // Convert to flow by wrapping the suspend function
                kotlinx.coroutines.flow.flow {
                    val responseFlow = genesisBridgeService.processRequest(
                        AiRequest(
                            query = message,
                            type = "fusion",
                            context = mapOf("userContext" to context, "orchestration" to "true")
                        )
                    )
                    emitAll(responseFlow)
                }
            }

            else -> {
                Log.e(TAG, "Unsupported sender type: $sender")
                null
            }
        }

        responseFlow?.let { flow ->
            viewModelScope.launch {
                try {
                    val responseMessage = flow.first()
                    _messages.update { current ->
                        current + AgentMessage(
                            from = sender.name,
                            content = responseMessage.content,
                            sender = sender,
                            timestamp = System.currentTimeMillis(),
                            confidence = responseMessage.confidence
                        )
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error processing AI response from $sender: ${e.message}", e)
                    _messages.update { current ->
                        current + AgentMessage(
                            from = AgentType.GENESIS.name,
                            content = "Error from ${sender.name}: ${e.message}",
                            sender = AgentType.GENESIS,
                            timestamp = System.currentTimeMillis(),
                            confidence = 0.0f
                        )
                    }
                }
            }
        }
    }

    // This `toggleAgent` was marked with `override` in user's snippet.
    /*override*/ fun toggleAgent(agent: AgentType) {
        _activeAgents.update { current ->
            if (current.contains(agent)) {
                current - agent
            } else {
                current + agent
            }
        }
    }

    fun selectAgent(agent: AgentType) {
        _selectedAgent.value = agent
    }

    fun toggleRecording() {
        if (_isRecording.value) {
            val result = neuralWhisper.stopRecording() // stopRecording now returns a string status
            Log.d(TAG, "Stopped recording. Status: $result")
            // isRecording state will be updated by NeuralWhisper's conversationState or directly
            _isRecording.value = false // Explicitly set here based on action
        } else {
            val started = neuralWhisper.startRecording()
            if (started) {
                Log.d(TAG, "Started recording.")
                _isRecording.value = true
            } else {
                Log.e(
                    TAG,
                    "Failed to start recording (NeuralWhisper.startRecording returned false)."
                )
                // Optionally update UI with error state
            }
        }
    }

    fun toggleTranscribing() {
        // For beta, link transcribing state to recording state or a separate logic if needed.
        // User's snippet implies this might be a simple toggle for now.
        _isTranscribing.update { !it } // Simple toggle
        Log.d(TAG, "Transcribing toggled to: ${_isTranscribing.value}")
        // If actual transcription process needs to be started/stopped in NeuralWhisper:
        // if (_isTranscribing.value) neuralWhisper.startTranscription() else neuralWhisper.stopTranscription()
    }
}
