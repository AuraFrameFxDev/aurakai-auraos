package dev.aurakai.auraframefx.network

import dev.aurakai.auraframefx.aura.themes.ThemeApi
import dev.aurakai.auraframefx.network.api.UserApi
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Wrapper class that aggregates all API interfaces for the Trinity system.
 * This provides a single point of access to all API services.
 */
@Singleton
class AuraApiServiceWrapper @Inject constructor(
    val userApi: UserApi,
    val aiAgentApi: AIAgentApi,
    val themeApi: ThemeApi
)
