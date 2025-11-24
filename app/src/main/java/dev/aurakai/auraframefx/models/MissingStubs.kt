package dev.aurakai.auraframefx.models

/**
 * Collection of commonly referenced types that are missing
 * These are stubs to allow compilation - implement properly later
 */

// Character types for embodiment
enum class Character {
    AURA,
    KAI,
    CASCADE,
    GENESIS,
    NEUTRAL
}

// Movement behaviors
enum class MovementBehavior {
    IDLE,
    WALKING,
    RUNNING,
    FLOATING,
    TELEPORTING
}

// Movement presets
object MovementPresets {
    const val SLOW = 0.5f
    const val NORMAL = 1.0f
    const val FAST = 2.0f
}

// Screen positions
object ScreenPositions {
    const val CENTER_LEFT = "center_left"
    const val CENTER_RIGHT = "center_right"
    const val TOP_CENTER = "top_center"
    const val BOTTOM_CENTER = "bottom_center"
}

// Visual effects
enum class VisualEffect {
    INTERFACE_PANEL,
    WALKING_AURA,
    SHIELD_CALM,
    SCIENTIST,
    GLOW,
    PARTICLE
}
