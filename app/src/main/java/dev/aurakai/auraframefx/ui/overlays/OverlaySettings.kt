package dev.aurakai.auraframefx.ui.overlays

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.setValue

class OverlaySettings(
    enabled: Boolean = true,
    order: List<String> = listOf("Vignette", "Agent Edge", "Aura Presence", "Chat Bubble", "Sidebar")
) {
    var overlaysEnabled by mutableStateOf(enabled)
    var overlayZOrder by mutableStateOf(order)
    var transitionStyle by mutableStateOf("lens")
    var transitionSpeed by mutableStateOf(3)
}

val LocalOverlaySettings = staticCompositionLocalOf { OverlaySettings() }
