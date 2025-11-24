package dev.aurakai.auraframefx.security

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Security state data class for monitoring
 */
data class SecurityState(
    val errorState: Boolean = false,
    val errorMessage: String? = null
)

/**
 * Genesis Security Context Interface
 */
interface SecurityContext {
    val securityState: StateFlow<SecurityState>
    val encryptionStatus: StateFlow<EncryptionStatus>
    val permissionsState: StateFlow<Map<String, Boolean>>
    val threatDetectionActive: StateFlow<Boolean>

    fun hasPermission(permission: String): Boolean
        _securityState.value = SecurityState(errorState = true, errorMessage = message)
    }

    /**
     * Clears any security error state.
     */
    fun clearSecurityError() {
        _securityState.value = SecurityState(errorState = false, errorMessage = null)
    }

    /**
     * Updates the encryption status.
     */
    fun setEncryptionStatus(status: EncryptionStatus) {
        _encryptionStatus.value = status
    }

    /**
     * Updates permissions state.
     */
    fun updatePermissions(permissions: Map<String, Boolean>) {
        _permissionsState.value = permissions
    }
}

data class ApplicationIntegrity(
    val signatureHash: String,
    val isValid: Boolean
)

data class SecurityEvent(
    val type: SecurityEventType,
    val details: String,
    val severity: EventSeverity
)

enum class SecurityEventType {
    INTEGRITY_CHECK,
    PERMISSION_VIOLATION,
    ACCESS_DENIED
}

enum class EventSeverity {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}