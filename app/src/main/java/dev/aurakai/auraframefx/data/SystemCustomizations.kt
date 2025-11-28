package dev.aurakai.auraframefx.data

import kotlinx.serialization.Serializable

/**
 * System customization settings for Genesis-OS
 */
@Serializable
data class SystemCustomizations(
    val theme: String = "cyberpunk_dark",
    val animationsEnabled: Boolean = true,
    val cyberpunkMode: Boolean = true,
    val performanceMode: String = "balanced",
    val notificationsEnabled: Boolean = true,
    val customColors: Map<String, String> = emptyMap(),
    val lastUpdated: Long = System.currentTimeMillis()
)
