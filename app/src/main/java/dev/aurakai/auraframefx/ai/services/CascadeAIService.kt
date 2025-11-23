package dev.aurakai.auraframefx.ai.services

import dev.aurakai.auraframefx.models.AiRequest

enum class CascadeAIService {
    ;

    annotation class ProcessRequestFlow(val value: AiRequest)

}
