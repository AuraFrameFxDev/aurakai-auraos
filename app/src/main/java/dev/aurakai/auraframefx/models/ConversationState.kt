package dev.aurakai.auraframefx.models

/**
 * Represents different conversation states for AI agents
 */
enum class ConversationState {
    IDLE,
    LISTENING,
    PROCESSING,
    RESPONDING,
    WAITING_FOR_INPUT,
    ERROR,
    THINKING,
    ANALYZING
}
