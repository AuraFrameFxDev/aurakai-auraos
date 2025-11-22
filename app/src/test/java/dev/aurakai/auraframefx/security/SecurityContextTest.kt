package dev.aurakai.auraframefx.security

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * Comprehensive test suite for SecurityContext and DefaultSecurityContext
 */
@ExperimentalCoroutinesApi
@DisplayName("SecurityContext Tests")
class SecurityContextTest {

    private lateinit var securityContext: DefaultSecurityContext

    @BeforeEach
    fun setUp() {
        securityContext = DefaultSecurityContext()
    }

    @Nested
    @DisplayName("Initial State Tests")
    inner class InitialStateTests {

        @Test
        @DisplayName("should initialize with default security state")
        fun shouldInitializeWithDefaultSecurityState() = runTest {
            // When
            val state = securityContext.securityState.first()

            // Then
            assertFalse(state.errorState)
            assertNull(state.errorMessage)
        }

        @Test
        @DisplayName("should initialize with NOT_INITIALIZED encryption status")
        fun shouldInitializeWithNotInitializedEncryptionStatus() = runTest {
            // When
            val status = securityContext.encryptionStatus.first()

            // Then
            assertEquals(EncryptionStatus.NOT_INITIALIZED, status)
        }

        @Test
        @DisplayName("should initialize with empty permissions state")
        fun shouldInitializeWithEmptyPermissionsState() = runTest {
            // When
            val permissions = securityContext.permissionsState.first()

            // Then
            assertTrue(permissions.isEmpty())
        }

        @Test
        @DisplayName("should initialize with threat detection inactive")
        fun shouldInitializeWithThreatDetectionInactive() = runTest {
            // When
            val isActive = securityContext.threatDetectionActive.first()

            // Then
            assertFalse(isActive)
        }
    }

    @Nested
    @DisplayName("Permission Tests")
    inner class PermissionTests {

        @Test
        @DisplayName("should grant all permissions by default")
        fun shouldGrantAllPermissionsByDefault() {
            // When & Then
            assertTrue(securityContext.hasPermission("READ"))
            assertTrue(securityContext.hasPermission("WRITE"))
            assertTrue(securityContext.hasPermission("EXECUTE"))
        }

        @Test
        @DisplayName("should handle null permission check")
        fun shouldHandleNullPermissionCheck() {
            // When & Then
            assertTrue(securityContext.hasPermission(""))
        }

        @Test
        @DisplayName("should handle special characters in permission")
        fun shouldHandleSpecialCharactersInPermission() {
            // When & Then
            assertTrue(securityContext.hasPermission("SPECIAL@#$"))
        }

        @Test
        @DisplayName("should update permissions state")
        fun shouldUpdatePermissionsState() = runTest {
            // Given
            val newPermissions = mapOf(
                "READ" to true,
                "WRITE" to false,
                "EXECUTE" to true
            )

            // When
            securityContext.updatePermissions(newPermissions)

            // Then
            val permissions = securityContext.permissionsState.first()
            assertEquals(3, permissions.size)
            assertEquals(true, permissions["READ"])
            assertEquals(false, permissions["WRITE"])
            assertEquals(true, permissions["EXECUTE"])
        }

        @Test
        @DisplayName("should handle empty permissions update")
        fun shouldHandleEmptyPermissionsUpdate() = runTest {
            // When
            securityContext.updatePermissions(emptyMap())

            // Then
            val permissions = securityContext.permissionsState.first()
            assertTrue(permissions.isEmpty())
        }
    }

    @Nested
    @DisplayName("User Management Tests")
    inner class UserManagementTests {

        @Test
        @DisplayName("should return default user")
        fun shouldReturnDefaultUser() {
            // When
            val user = securityContext.getCurrentUser()

            // Then
            assertEquals("genesis_user", user)
        }

        @Test
        @DisplayName("should not return null user")
        fun shouldNotReturnNullUser() {
            // When
            val user = securityContext.getCurrentUser()

            // Then
            assertNotNull(user)
        }
    }

    @Nested
    @DisplayName("Secure Mode Tests")
    inner class SecureModeTests {

        @Test
        @DisplayName("should default to non-secure mode")
        fun shouldDefaultToNonSecureMode() {
            // When
            val isSecure = securityContext.isSecureMode()

            // Then
            assertFalse(isSecure)
        }
    }

    @Nested
    @DisplayName("Access Validation Tests")
    inner class AccessValidationTests {

        @Test
        @DisplayName("should validate access to any resource")
        fun shouldValidateAccessToAnyResource() {
            // When & Then
            assertTrue(securityContext.validateAccess("/path/to/resource"))
            assertTrue(securityContext.validateAccess("file.txt"))
            assertTrue(securityContext.validateAccess(""))
        }

        @Test
        @DisplayName("should validate access with special characters")
        fun shouldValidateAccessWithSpecialCharacters() {
            // When & Then
            assertTrue(securityContext.validateAccess("/path/with spaces/file"))
            assertTrue(securityContext.validateAccess("/path/with@#$/file"))
        }

        @Test
        @DisplayName("should validate access with Unicode characters")
        fun shouldValidateAccessWithUnicodeCharacters() {
            // When & Then
            assertTrue(securityContext.validateAccess("/путь/файл"))
            assertTrue(securityContext.validateAccess("/路径/文件"))
        }
    }

    @Nested
    @DisplayName("Application Integrity Tests")
    inner class ApplicationIntegrityTests {

        @Test
        @DisplayName("should verify application integrity successfully")
        fun shouldVerifyApplicationIntegritySuccessfully() {
            // When
            val integrity = securityContext.verifyApplicationIntegrity()

            // Then
            assertTrue(integrity.isValid)
            assertNotNull(integrity.signatureHash)
        }

        @Test
        @DisplayName("should return consistent signature hash")
        fun shouldReturnConsistentSignatureHash() {
            // When
            val integrity1 = securityContext.verifyApplicationIntegrity()
            val integrity2 = securityContext.verifyApplicationIntegrity()

            // Then
            assertEquals(integrity1.signatureHash, integrity2.signatureHash)
        }

        @Test
        @DisplayName("should have default signature hash")
        fun shouldHaveDefaultSignatureHash() {
            // When
            val integrity = securityContext.verifyApplicationIntegrity()

            // Then
            assertEquals("default_signature_hash", integrity.signatureHash)
        }
    }

    @Nested
    @DisplayName("Security Event Logging Tests")
    inner class SecurityEventLoggingTests {

        @Test
        @DisplayName("should log integrity check event")
        fun shouldLogIntegrityCheckEvent() {
            // Given
            val event = SecurityEvent(
                type = SecurityEventType.INTEGRITY_CHECK,
                details = "System integrity verified",
                severity = EventSeverity.LOW
            )

            // When & Then
            assertDoesNotThrow {
                securityContext.logSecurityEvent(event)
            }
        }

        @Test
        @DisplayName("should log permission violation event")
        fun shouldLogPermissionViolationEvent() {
            // Given
            val event = SecurityEvent(
                type = SecurityEventType.PERMISSION_VIOLATION,
                details = "Unauthorized access attempt",
                severity = EventSeverity.HIGH
            )

            // When & Then
            assertDoesNotThrow {
                securityContext.logSecurityEvent(event)
            }
        }

        @Test
        @DisplayName("should log access denied event")
        fun shouldLogAccessDeniedEvent() {
            // Given
            val event = SecurityEvent(
                type = SecurityEventType.ACCESS_DENIED,
                details = "Access to resource denied",
                severity = EventSeverity.MEDIUM
            )

            // When & Then
            assertDoesNotThrow {
                securityContext.logSecurityEvent(event)
            }
        }

        @Test
        @DisplayName("should handle critical severity events")
        fun shouldHandleCriticalSeverityEvents() {
            // Given
            val event = SecurityEvent(
                type = SecurityEventType.INTEGRITY_CHECK,
                details = "Critical security breach detected",
                severity = EventSeverity.CRITICAL
            )

            // When & Then
            assertDoesNotThrow {
                securityContext.logSecurityEvent(event)
            }
        }
    }

    @Nested
    @DisplayName("Threat Detection Tests")
    inner class ThreatDetectionTests {

        @Test
        @DisplayName("should start threat detection")
        fun shouldStartThreatDetection() = runTest {
            // When
            securityContext.startThreatDetection()

            // Then
            val isActive = securityContext.threatDetectionActive.first()
            assertTrue(isActive)
        }

        @Test
        @DisplayName("should activate encryption when starting threat detection")
        fun shouldActivateEncryptionWhenStartingThreatDetection() = runTest {
            // When
            securityContext.startThreatDetection()

            // Then
            val status = securityContext.encryptionStatus.first()
            assertEquals(EncryptionStatus.ACTIVE, status)
        }

        @Test
        @DisplayName("should stop threat detection")
        fun shouldStopThreatDetection() = runTest {
            // Given
            securityContext.startThreatDetection()

            // When
            securityContext.stopThreatDetection()

            // Then
            val isActive = securityContext.threatDetectionActive.first()
            assertFalse(isActive)
        }

        @Test
        @DisplayName("should handle multiple start calls")
        fun shouldHandleMultipleStartCalls() = runTest {
            // When
            securityContext.startThreatDetection()
            securityContext.startThreatDetection()

            // Then
            val isActive = securityContext.threatDetectionActive.first()
            assertTrue(isActive)
        }

        @Test
        @DisplayName("should handle multiple stop calls")
        fun shouldHandleMultipleStopCalls() = runTest {
            // Given
            securityContext.startThreatDetection()

            // When
            securityContext.stopThreatDetection()
            securityContext.stopThreatDetection()

            // Then
            val isActive = securityContext.threatDetectionActive.first()
            assertFalse(isActive)
        }
    }

    @Nested
    @DisplayName("Security Error State Tests")
    inner class SecurityErrorStateTests {

        @Test
        @DisplayName("should set security error")
        fun shouldSetSecurityError() = runTest {
            // When
            securityContext.setSecurityError("Test error message")

            // Then
            val state = securityContext.securityState.first()
            assertTrue(state.errorState)
            assertEquals("Test error message", state.errorMessage)
        }

        @Test
        @DisplayName("should clear security error")
        fun shouldClearSecurityError() = runTest {
            // Given
            securityContext.setSecurityError("Error")

            // When
            securityContext.clearSecurityError()

            // Then
            val state = securityContext.securityState.first()
            assertFalse(state.errorState)
            assertNull(state.errorMessage)
        }

        @Test
        @DisplayName("should update error message")
        fun shouldUpdateErrorMessage() = runTest {
            // Given
            securityContext.setSecurityError("First error")

            // When
            securityContext.setSecurityError("Second error")

            // Then
            val state = securityContext.securityState.first()
            assertEquals("Second error", state.errorMessage)
        }

        @Test
        @DisplayName("should handle empty error message")
        fun shouldHandleEmptyErrorMessage() = runTest {
            // When
            securityContext.setSecurityError("")

            // Then
            val state = securityContext.securityState.first()
            assertTrue(state.errorState)
            assertEquals("", state.errorMessage)
        }
    }

    @Nested
    @DisplayName("Encryption Status Tests")
    inner class EncryptionStatusTests {

        @Test
        @DisplayName("should set encryption status to ACTIVE")
        fun shouldSetEncryptionStatusToActive() = runTest {
            // When
            securityContext.setEncryptionStatus(EncryptionStatus.ACTIVE)

            // Then
            val status = securityContext.encryptionStatus.first()
            assertEquals(EncryptionStatus.ACTIVE, status)
        }

        @Test
        @DisplayName("should set encryption status to DISABLED")
        fun shouldSetEncryptionStatusToDisabled() = runTest {
            // When
            securityContext.setEncryptionStatus(EncryptionStatus.DISABLED)

            // Then
            val status = securityContext.encryptionStatus.first()
            assertEquals(EncryptionStatus.DISABLED, status)
        }

        @Test
        @DisplayName("should set encryption status to ERROR")
        fun shouldSetEncryptionStatusToError() = runTest {
            // When
            securityContext.setEncryptionStatus(EncryptionStatus.ERROR)

            // Then
            val status = securityContext.encryptionStatus.first()
            assertEquals(EncryptionStatus.ERROR, status)
        }

        @Test
        @DisplayName("should update encryption status multiple times")
        fun shouldUpdateEncryptionStatusMultipleTimes() = runTest {
            // When
            securityContext.setEncryptionStatus(EncryptionStatus.ACTIVE)
            securityContext.setEncryptionStatus(EncryptionStatus.DISABLED)
            securityContext.setEncryptionStatus(EncryptionStatus.ACTIVE)

            // Then
            val status = securityContext.encryptionStatus.first()
            assertEquals(EncryptionStatus.ACTIVE, status)
        }
    }

    @Nested
    @DisplayName("Data Class Tests")
    inner class DataClassTests {

        @Test
        @DisplayName("should create SecurityState with error")
        fun shouldCreateSecurityStateWithError() {
            // When
            val state = SecurityState(errorState = true, errorMessage = "Error")

            // Then
            assertTrue(state.errorState)
            assertEquals("Error", state.errorMessage)
        }

        @Test
        @DisplayName("should create SecurityState without error")
        fun shouldCreateSecurityStateWithoutError() {
            // When
            val state = SecurityState(errorState = false, errorMessage = null)

            // Then
            assertFalse(state.errorState)
            assertNull(state.errorMessage)
        }

        @Test
        @DisplayName("should create ApplicationIntegrity")
        fun shouldCreateApplicationIntegrity() {
            // When
            val integrity = ApplicationIntegrity(
                signatureHash = "hash123",
                isValid = true
            )

            // Then
            assertEquals("hash123", integrity.signatureHash)
            assertTrue(integrity.isValid)
        }

        @Test
        @DisplayName("should create SecurityEvent with all properties")
        fun shouldCreateSecurityEventWithAllProperties() {
            // When
            val event = SecurityEvent(
                type = SecurityEventType.INTEGRITY_CHECK,
                details = "Check completed",
                severity = EventSeverity.LOW
            )

            // Then
            assertEquals(SecurityEventType.INTEGRITY_CHECK, event.type)
            assertEquals("Check completed", event.details)
            assertEquals(EventSeverity.LOW, event.severity)
        }
    }

    @Nested
    @DisplayName("Enum Tests")
    inner class EnumTests {

        @Test
        @DisplayName("should have all encryption status values")
        fun shouldHaveAllEncryptionStatusValues() {
            // When & Then
            val values = EncryptionStatus.values()
            assertTrue(values.contains(EncryptionStatus.ACTIVE))
            assertTrue(values.contains(EncryptionStatus.NOT_INITIALIZED))
            assertTrue(values.contains(EncryptionStatus.DISABLED))
            assertTrue(values.contains(EncryptionStatus.ERROR))
        }

        @Test
        @DisplayName("should have all security event types")
        fun shouldHaveAllSecurityEventTypes() {
            // When & Then
            val values = SecurityEventType.values()
            assertTrue(values.contains(SecurityEventType.INTEGRITY_CHECK))
            assertTrue(values.contains(SecurityEventType.PERMISSION_VIOLATION))
            assertTrue(values.contains(SecurityEventType.ACCESS_DENIED))
        }

        @Test
        @DisplayName("should have all event severity levels")
        fun shouldHaveAllEventSeverityLevels() {
            // When & Then
            val values = EventSeverity.values()
            assertTrue(values.contains(EventSeverity.LOW))
            assertTrue(values.contains(EventSeverity.MEDIUM))
            assertTrue(values.contains(EventSeverity.HIGH))
            assertTrue(values.contains(EventSeverity.CRITICAL))
        }
    }
}