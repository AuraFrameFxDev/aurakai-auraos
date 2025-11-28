package dev.aurakai.auraframefx.data

import kotlinx.serialization.Serializable

/**
 * Security policy configuration for Genesis-OS
 */
@Serializable
data class SecurityPolicy(
    val securityLevel: String = "standard",
    val biometricEnabled: Boolean = false,
    val encryptionEnabled: Boolean = true,
    val requireAuth: Boolean = true,
    val permissions: Map<String, Boolean> = emptyMap(),
    val lastUpdated: Long = System.currentTimeMillis()
)
