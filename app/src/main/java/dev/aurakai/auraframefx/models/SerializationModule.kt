package dev.aurakai.auraframefx.models

import dev.aurakai.auraframefx.system.lockscreen.model.HapticFeedbackConfig
import dev.aurakai.auraframefx.system.lockscreen.model.LockScreenAnimationConfig
import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

val AeGenesisSerializersModule = SerializersModule {
    // Core type serializers
    contextual(Any::class) { AnySerializer }
    contextual(Instant::class) { InstantSerializer }
}

val AuraFrameSerializersModule = AeGenesisSerializersModule

object AnySerializer : KSerializer<Any> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Any", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Any) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): Any = decoder.decodeString()
}

object InstantSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Instant) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): Instant = Instant.parse(decoder.decodeString())
}
