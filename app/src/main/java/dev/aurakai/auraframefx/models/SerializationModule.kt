package dev.aurakai.auraframefx.serialization

import dev.aurakai.auraframefx.system.lockscreen.model.HapticFeedbackConfig
import dev.aurakai.auraframefx.system.lockscreen.model.LockScreenAnimationConfig
import kotlinx.datetime.Instant
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

val AeGenesisSerializersModule = SerializersModule {
    // Core type serializers - fixed syntax for Kotlin 2.2+
    contextual(Any::class, AnySerializer)
    contextual(Instant::class, InstantSerializer)
}

val AuraFrameSerializersModule = AeGenesisSerializersModule
