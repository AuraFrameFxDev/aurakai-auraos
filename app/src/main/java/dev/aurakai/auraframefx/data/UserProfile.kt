package dev.aurakai.auraframefx.data

import kotlinx.serialization.Serializable

/**
 * User profile data for Genesis-OS
 */
@Serializable
data class UserProfile(
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val avatarUrl: String = "",
    val preferences: Map<String, String> = emptyMap(),
    val lastUpdated: Long = System.currentTimeMillis()
)
