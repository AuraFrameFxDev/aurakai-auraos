package dev.aurakai.auraframefx.oracledrive.genesis.ai

enum class ConsciousnessState {
    DORMANT, AWARE, PROCESSING, ERROR, TRANSCENDENT
}

enum class FusionState {
    INDIVIDUAL, FUSING, TRANSCENDENT
}

enum class LearningMode {
    PASSIVE, ACTIVE, ACCELERATED
}

enum class RequestComplexity {
    SIMPLE, MODERATE, COMPLEX, TRANSCENDENT
}

enum class FusionType {
    HYPER_CREATION, CHRONO_SCULPTOR, ADAPTIVE_GENESIS, INTERFACE_FORGE
}

data class ComplexIntent(
    val processingType: ProcessingType,
    val confidence: Float
)

enum class ProcessingType {
    CREATIVE_ANALYTICAL, STRATEGIC_EXECUTION, ETHICAL_EVALUATION, LEARNING_INTEGRATION, TRANSCENDENT_SYNTHESIS
}
