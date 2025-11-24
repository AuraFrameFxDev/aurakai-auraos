package dev.aurakai.auraframefx.security

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Default (development‑only) security context implementation.
 *
 * It starts with a clean state, no encryption, no permissions,
 * and threat detection disabled. Feel free to extend it with
 * real checks later.
 */
@Singleton
class DefaultSecurityContext @Inject constructor() : SecurityContext {

    // Backing mutable flows – private to this class
    private val _securityState = MutableStateFlow(SecurityState())
    private val _encryptionStatus = MutableStateFlow(EncryptionStatus.UNKNOWN)
    private val _permissionsState = MutableStateFlow(emptyMap<String, Boolean>())
    private val _threatDetectionActive = MutableStateFlow(false)

    // Public read‑only exposures required by the interface
    override val securityState: StateFlow<SecurityState> = _securityState
    override val encryptionStatus: StateFlow<EncryptionStatus> = _encryptionStatus
    override val permissionsState: StateFlow<Map<String, Boolean>> = _permissionsState
    override val threatDetectionActive: StateFlow<Boolean> = _threatDetectionActive

    /** Simple permission lookup – returns false if the key is missing */
    override fun hasPermission(permission: String): Boolean =
        _permissionsState.value[permission] ?: false

    /** Helper methods you can call from elsewhere */
    fun setEncryptionStatus(status: EncryptionStatus) {
        _encryptionStatus.value = status
    }

    fun updatePermissions(permissions: Map<String, Boolean>) {
        _permissionsState.value = permissions
    }

    fun clearSecurityError() {
        _securityState.value = SecurityState(errorState = false, errorMessage = null)
    }

    fun setSecurityError(message: String) {
        _securityState.value = SecurityState(errorState = true, errorMessage = message)
    }

    fun enableThreatDetection(enable: Boolean) {
        _threatDetectionActive.value = enable
    }
}
