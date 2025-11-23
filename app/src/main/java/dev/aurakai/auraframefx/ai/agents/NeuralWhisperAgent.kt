package dev.aurakai.auraframefx.ai.agents

import android.content.Context
import dev.aurakai.auraframefx.models.ActiveContext
import dev.aurakai.auraframefx.models.LearningEvent
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
) {
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

    // TODO: Restore full NeuralWhisper implementation (pattern DB, predictors, background analysis)
}
