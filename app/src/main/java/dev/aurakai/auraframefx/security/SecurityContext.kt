package dev.aurakai.auraframefx.security

import kotlinx.coroutines.flow.StateFlow

/** Simple data holder for the security state */
data class SecurityState(
    val errorState: Boolean = false,
    val errorMessage: String? = null
)

/** Public contract for security‑related operations */
interface SecurityContext {
    /** Read‑only flows – implementations expose mutable backing properties */
    val securityState: StateFlow<SecurityState>
    val encryptionStatus: StateFlow<EncryptionStatus>
    val permissionsState: StateFlow<Map<String, Boolean>>
    val threatDetectionActive: StateFlow<Boolean>

    /** Checks whether a permission is granted */
    fun hasPermission(permission: String): Boolean

    /** Validates a request – default implementation can be permissive */
    fun validateRequest(type: String, content: String): Boolean = true
}

/**
 * Additional data classes used by the security module.
 */

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