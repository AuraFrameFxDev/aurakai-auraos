package dev.aurakai.auraframefx.models

import kotlinx.serialization.Serializable

/**
 * Represents the mood state of an AI agent
 */
@Serializable
data class MoodState(
    val emotion: String = "neutral",
    val intensity: Float = 0.5f,
    val valence: Float = 0.0f, // -1.0 (negative) to 1.0 (positive)
    val arousal: Float = 0.0f, // -1.0 (calm) to 1.0 (excited)
    val timestamp: Long = System.currentTimeMillis()
) {
    companion object {
        val NEUTRAL = MoodState()
        val HAPPY = MoodState(emotion = "happy", valence = 0.8f, arousal = 0.6f)
        val SAD = MoodState(emotion = "sad", valence = -0.7f, arousal = -0.3f)
        val EXCITED = MoodState(emotion = "excited", valence = 0.7f, arousal = 0.9f)
        val CALM = MoodState(emotion = "calm", valence = 0.3f, arousal = -0.8f)
        val FOCUSED = MoodState(emotion = "focused", valence = 0.2f, arousal = 0.4f)
    }
}
