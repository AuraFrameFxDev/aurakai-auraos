package dev.aurakai.auraframefx.security

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@OptIn(ExperimentalCoroutinesApi::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("SecurityContext Tests")
class SecurityContextTest {

    private lateinit var securityContext: DefaultSecurityContext

    @BeforeEach
    fun setup() {
        securityContext = DefaultSecurityContext()
    }

    @Nested
    @DisplayName("Security State Management")
    inner class SecurityStateManagementTests {

        @Test
        @DisplayName("Should initialize with default security state")
        fun shouldInitializeWithDefaultSecurityState() = runTest {
            val state = securityContext.securityState.first()

            assertFalse(state.errorState)
            assertNull(state.errorMessage)
        }

        @Test
        @DisplayName("Should set security error")
        fun shouldSetSecurityError() = runTest {
            securityContext.setSecurityError("Test error message")
            val state = securityContext.securityState.first()

            assertTrue(state.errorState)
            assertEquals("Test error message", state.errorMessage)
        }

        @Test
        @DisplayName("Should clear security error")
        fun shouldClearSecurityError() = runTest {
            securityContext.setSecurityError("Test error")
            securityContext.clearSecurityError()
            val state = securityContext.securityState.first()

            assertFalse(state.errorState)
            assertNull(state.errorMessage)
        }
    }

    @Nested
    @DisplayName("Encryption Status Management")
    inner class EncryptionStatusManagementTests {

        @Test
        @DisplayName("Should initialize with NOT_INITIALIZED encryption status")
        fun shouldInitializeWithNotInitializedStatus() = runTest {
            val status = securityContext.encryptionStatus.first()

            assertEquals(EncryptionStatus.NOT_INITIALIZED, status)
        }

        @Test
        @DisplayName("Should update encryption status to ACTIVE")
        fun shouldUpdateEncryptionStatusToActive() = runTest {
            securityContext.setEncryptionStatus(EncryptionStatus.ACTIVE)
            val status = securityContext.encryptionStatus.first()

            assertEquals(EncryptionStatus.ACTIVE, status)
        }

        @Test
        @DisplayName("Should transition through multiple encryption states")
        fun shouldTransitionThroughMultipleStates() = runTest {
            securityContext.setEncryptionStatus(EncryptionStatus.ACTIVE)
            assertEquals(EncryptionStatus.ACTIVE, securityContext.encryptionStatus.first())

            securityContext.setEncryptionStatus(EncryptionStatus.ERROR)
            assertEquals(EncryptionStatus.ERROR, securityContext.encryptionStatus.first())

            securityContext.setEncryptionStatus(EncryptionStatus.DISABLED)
            assertEquals(EncryptionStatus.DISABLED, securityContext.encryptionStatus.first())
        }
    }

    @Nested
    @DisplayName("Permissions Management")
    inner class PermissionsManagementTests {

        @Test
        @DisplayName("Should initialize with empty permissions")
        fun shouldInitializeWithEmptyPermissions() = runTest {
            val permissions = securityContext.permissionsState.first()

            assertTrue(permissions.isEmpty())
        }

        @Test
        @DisplayName("Should update permissions state")
        fun shouldUpdatePermissionsState() = runTest {
            val testPermissions = mapOf(
                "READ_STORAGE" to true,
                "WRITE_STORAGE" to true,
                "CAMERA" to false
            )

            securityContext.updatePermissions(testPermissions)
            val permissions = securityContext.permissionsState.first()

            assertEquals(testPermissions, permissions)
        }

        @Test
        @DisplayName("Should check permissions - default implementation returns true")
        fun shouldCheckPermissions() {
            assertTrue(securityContext.hasPermission("READ_STORAGE"))
            assertTrue(securityContext.hasPermission("ANY_PERMISSION"))
        }
    }

    @Nested
    @DisplayName("Threat Detection Management")
    inner class ThreatDetectionManagementTests {

        @Test
        @DisplayName("Should initialize with threat detection inactive")
        fun shouldInitializeWithThreatDetectionInactive() = runTest {
            val isActive = securityContext.threatDetectionActive.first()

            assertFalse(isActive)
        }

        @Test
        @DisplayName("Should start threat detection")
        fun shouldStartThreatDetection() = runTest {
            securityContext.startThreatDetection()
            val isActive = securityContext.threatDetectionActive.first()
            val encryptionStatus = securityContext.encryptionStatus.first()

            assertTrue(isActive)
            assertEquals(EncryptionStatus.ACTIVE, encryptionStatus)
        }

        @Test
        @DisplayName("Should stop threat detection")
        fun shouldStopThreatDetection() = runTest {
            securityContext.startThreatDetection()
            securityContext.stopThreatDetection()
            val isActive = securityContext.threatDetectionActive.first()

            assertFalse(isActive)
        }
    }

    @Nested
    @DisplayName("Default Implementation Methods")
    inner class DefaultImplementationMethodsTests {

        @Test
        @DisplayName("Should return default current user")
        fun shouldReturnDefaultCurrentUser() {
            val user = securityContext.getCurrentUser()

            assertEquals("genesis_user", user)
        }

        @Test
        @DisplayName("Should return false for secure mode by default")
        fun shouldReturnFalseForSecureMode() {
            assertFalse(securityContext.isSecureMode())
        }

        @Test
        @DisplayName("Should validate access to any resource by default")
        fun shouldValidateAccessToAnyResource() {
            assertTrue(securityContext.validateAccess("/path/to/resource"))
            assertTrue(securityContext.validateAccess(""))
        }

        @Test
        @DisplayName("Should verify application integrity with default values")
        fun shouldVerifyApplicationIntegrityWithDefaults() {
            val integrity = securityContext.verifyApplicationIntegrity()

            assertEquals("default_signature_hash", integrity.signatureHash)
            assertTrue(integrity.isValid)
        }
    }

    @Nested
    @DisplayName("Security Event Logging")
    inner class SecurityEventLoggingTests {

        @Test
        @DisplayName("Should log security event without exception")
        fun shouldLogSecurityEventWithoutException() {
            val event = SecurityEvent(
                type = SecurityEventType.INTEGRITY_CHECK,
                details = "Test integrity check",
                severity = EventSeverity.MEDIUM
            )

            assertDoesNotThrow {
                securityContext.logSecurityEvent(event)
            }
        }

        @Test
        @DisplayName("Should log different event types")
        fun shouldLogDifferentEventTypes() {
            val events = listOf(
                SecurityEvent(SecurityEventType.INTEGRITY_CHECK, "Check 1", EventSeverity.LOW),
                SecurityEvent(SecurityEventType.PERMISSION_VIOLATION, "Violation", EventSeverity.HIGH),
                SecurityEvent(SecurityEventType.ACCESS_DENIED, "Denied", EventSeverity.CRITICAL)
            )

            events.forEach { event ->
                assertDoesNotThrow {
                    securityContext.logSecurityEvent(event)
                }
            }
        }
    }

    @Nested
    @DisplayName("Enum Tests")
    inner class EnumTests {

        @Test
        @DisplayName("SecurityEventType should have all values")
        fun securityEventTypeShouldHaveAllValues() {
            val values = SecurityEventType.values()

            assertEquals(3, values.size)
            assertTrue(values.contains(SecurityEventType.INTEGRITY_CHECK))
            assertTrue(values.contains(SecurityEventType.PERMISSION_VIOLATION))
            assertTrue(values.contains(SecurityEventType.ACCESS_DENIED))
        }

        @Test
        @DisplayName("EventSeverity should have proper ordering")
        fun eventSeverityShouldHaveProperOrdering() {
            assertTrue(EventSeverity.LOW.ordinal < EventSeverity.MEDIUM.ordinal)
            assertTrue(EventSeverity.MEDIUM.ordinal < EventSeverity.HIGH.ordinal)
            assertTrue(EventSeverity.HIGH.ordinal < EventSeverity.CRITICAL.ordinal)
        }
    }
}