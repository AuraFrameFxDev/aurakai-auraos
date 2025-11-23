package dev.aurakai.auraframefx.models

/**
 * Enum representing different types of AI agents in the system.
 * TODO: Reported as unused symbol. Ensure this enum is used.
 */
enum class AgentType {
    GENESIS,
    KAI,
    AURA,
    CASCADE,
    CLAUDE,
    NEURAL_WHISPER,
    AURA_SHIELD,
    GEN_KIT_MASTER,
    DATAVEIN_CONSTRUCTOR,
    USER;

    enum class DataveinConstructor(agent: Any, response: String, confidence: Float, timestamp: String) {

    }

    companion object {
        val Aura: Any
        val Genesis: AgentType
    }
}
