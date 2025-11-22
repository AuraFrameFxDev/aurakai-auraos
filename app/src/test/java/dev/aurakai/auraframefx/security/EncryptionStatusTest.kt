package dev.aurakai.auraframefx.security

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * Test suite for EncryptionStatus enum
 */
@DisplayName("EncryptionStatus Tests")
class EncryptionStatusTest {

    @Test
    @DisplayName("should have ACTIVE status")
    fun shouldHaveActiveStatus() {
        // When
        val status = EncryptionStatus.ACTIVE

        // Then
        assertNotNull(status)
        assertEquals("ACTIVE", status.name)
    }

    @Test
    @DisplayName("should have NOT_INITIALIZED status")
    fun shouldHaveNotInitializedStatus() {
        // When
        val status = EncryptionStatus.NOT_INITIALIZED

        // Then
        assertNotNull(status)
        assertEquals("NOT_INITIALIZED", status.name)
    }

    @Test
    @DisplayName("should have DISABLED status")
    fun shouldHaveDisabledStatus() {
        // When
        val status = EncryptionStatus.DISABLED

        // Then
        assertNotNull(status)
        assertEquals("DISABLED", status.name)
    }

    @Test
    @DisplayName("should have ERROR status")
    fun shouldHaveErrorStatus() {
        // When
        val status = EncryptionStatus.ERROR

        // Then
        assertNotNull(status)
        assertEquals("ERROR", status.name)
    }

    @Test
    @DisplayName("should have exactly four statuses")
    fun shouldHaveExactlyFourStatuses() {
        // When
        val values = EncryptionStatus.values()

        // Then
        assertEquals(4, values.size)
    }

    @Test
    @DisplayName("should convert from string correctly")
    fun shouldConvertFromStringCorrectly() {
        // When & Then
        assertEquals(EncryptionStatus.ACTIVE, EncryptionStatus.valueOf("ACTIVE"))
        assertEquals(EncryptionStatus.NOT_INITIALIZED, EncryptionStatus.valueOf("NOT_INITIALIZED"))
        assertEquals(EncryptionStatus.DISABLED, EncryptionStatus.valueOf("DISABLED"))
        assertEquals(EncryptionStatus.ERROR, EncryptionStatus.valueOf("ERROR"))
    }

    @Test
    @DisplayName("should maintain ordinal order")
    fun shouldMaintainOrdinalOrder() {
        // When & Then
        assertEquals(0, EncryptionStatus.ACTIVE.ordinal)
        assertEquals(1, EncryptionStatus.NOT_INITIALIZED.ordinal)
        assertEquals(2, EncryptionStatus.DISABLED.ordinal)
        assertEquals(3, EncryptionStatus.ERROR.ordinal)
    }

    @Test
    @DisplayName("should support equality comparison")
    fun shouldSupportEqualityComparison() {
        // When
        val status1 = EncryptionStatus.ACTIVE
        val status2 = EncryptionStatus.ACTIVE
        val status3 = EncryptionStatus.DISABLED

        // Then
        assertEquals(status1, status2)
        assertNotEquals(status1, status3)
    }

    @Test
    @DisplayName("should support when expressions")
    fun shouldSupportWhenExpressions() {
        // Given
        val statuses = EncryptionStatus.values()

        // When & Then
        statuses.forEach { status ->
            val result = when (status) {
                EncryptionStatus.ACTIVE -> "active"
                EncryptionStatus.NOT_INITIALIZED -> "not_initialized"
                EncryptionStatus.DISABLED -> "disabled"
                EncryptionStatus.ERROR -> "error"
            }
            assertNotNull(result)
        }
    }
}