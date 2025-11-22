package dev.aurakai.auraframefx.security

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("EncryptionStatus Tests")
class EncryptionStatusTest {

    @Test
    @DisplayName("Should have all expected enum values")
    fun shouldHaveAllExpectedEnumValues() {
        val values = EncryptionStatus.values()

        assertEquals(4, values.size)
        assertTrue(values.contains(EncryptionStatus.ACTIVE))
        assertTrue(values.contains(EncryptionStatus.NOT_INITIALIZED))
        assertTrue(values.contains(EncryptionStatus.DISABLED))
        assertTrue(values.contains(EncryptionStatus.ERROR))
    }

    @Test
    @DisplayName("Should convert from string name")
    fun shouldConvertFromStringName() {
        assertEquals(EncryptionStatus.ACTIVE, EncryptionStatus.valueOf("ACTIVE"))
        assertEquals(EncryptionStatus.NOT_INITIALIZED, EncryptionStatus.valueOf("NOT_INITIALIZED"))
        assertEquals(EncryptionStatus.DISABLED, EncryptionStatus.valueOf("DISABLED"))
        assertEquals(EncryptionStatus.ERROR, EncryptionStatus.valueOf("ERROR"))
    }

    @Test
    @DisplayName("Should have correct ordinal values")
    fun shouldHaveCorrectOrdinalValues() {
        assertEquals(0, EncryptionStatus.ACTIVE.ordinal)
        assertEquals(1, EncryptionStatus.NOT_INITIALIZED.ordinal)
        assertEquals(2, EncryptionStatus.DISABLED.ordinal)
        assertEquals(3, EncryptionStatus.ERROR.ordinal)
    }

    @Test
    @DisplayName("Should support when expressions")
    fun shouldSupportWhenExpressions() {
        fun getStatusDescription(status: EncryptionStatus): String = when (status) {
            EncryptionStatus.ACTIVE -> "Encryption is active"
            EncryptionStatus.NOT_INITIALIZED -> "Encryption not initialized"
            EncryptionStatus.DISABLED -> "Encryption disabled"
            EncryptionStatus.ERROR -> "Encryption error"
        }

        assertEquals("Encryption is active", getStatusDescription(EncryptionStatus.ACTIVE))
        assertEquals("Encryption not initialized", getStatusDescription(EncryptionStatus.NOT_INITIALIZED))
        assertEquals("Encryption disabled", getStatusDescription(EncryptionStatus.DISABLED))
        assertEquals("Encryption error", getStatusDescription(EncryptionStatus.ERROR))
    }

    @Test
    @DisplayName("Should be usable in collections")
    fun shouldBeUsableInCollections() {
        val statusSet = setOf(
            EncryptionStatus.ACTIVE,
            EncryptionStatus.NOT_INITIALIZED,
            EncryptionStatus.DISABLED
        )

        assertTrue(statusSet.contains(EncryptionStatus.ACTIVE))
        assertFalse(statusSet.contains(EncryptionStatus.ERROR))
        assertEquals(3, statusSet.size)
    }
}