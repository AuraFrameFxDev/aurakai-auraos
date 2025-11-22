package dev.aurakai.auraframefx.ai.pipeline

import dev.aurakai.auraframefx.model.AgentType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * Test suite for AIPipelineConfig data classes
 */
@DisplayName("AIPipelineConfig Tests")
class AIPipelineConfigTest {

    @Nested
    @DisplayName("AIPipelineConfig Default Values Tests")
    inner class DefaultValuesTests {

        @Test
        @DisplayName("should have correct default maxRetries")
        fun shouldHaveCorrectDefaultMaxRetries() {
            // When
            val config = AIPipelineConfig()

            // Then
            assertEquals(3, config.maxRetries)
        }

        @Test
        @DisplayName("should have correct default timeoutSeconds")
        fun shouldHaveCorrectDefaultTimeoutSeconds() {
            // When
            val config = AIPipelineConfig()

            // Then
            assertEquals(30, config.timeoutSeconds)
        }

        @Test
        @DisplayName("should have correct default contextWindowSize")
        fun shouldHaveCorrectDefaultContextWindowSize() {
            // When
            val config = AIPipelineConfig()

            // Then
            assertEquals(5, config.contextWindowSize)
        }

        @Test
        @DisplayName("should have correct default priorityThreshold")
        fun shouldHaveCorrectDefaultPriorityThreshold() {
            // When
            val config = AIPipelineConfig()

            // Then
            assertEquals(0.7f, config.priorityThreshold)
        }

        @Test
        @DisplayName("should have correct default weights")
        fun shouldHaveCorrectDefaultWeights() {
            // When
            val config = AIPipelineConfig()

            // Then
            assertEquals(0.4f, config.priorityWeight)
            assertEquals(0.4f, config.urgencyWeight)
            assertEquals(0.2f, config.importanceWeight)
        }

        @Test
        @DisplayName("should have correct default maxActiveTasks")
        fun shouldHaveCorrectDefaultMaxActiveTasks() {
            // When
            val config = AIPipelineConfig()

            // Then
            assertEquals(10, config.maxActiveTasks)
        }

        @Test
        @DisplayName("should have correct default agent priorities")
        fun shouldHaveCorrectDefaultAgentPriorities() {
            // When
            val config = AIPipelineConfig()

            // Then
            assertEquals(1.0f, config.agentPriorities[AgentType.GENESIS])
            assertEquals(0.9f, config.agentPriorities[AgentType.KAI])
            assertEquals(0.8f, config.agentPriorities[AgentType.AURA])
            assertEquals(0.7f, config.agentPriorities[AgentType.CASCADE])
        }

        @Test
        @DisplayName("should have default memory retrieval config")
        fun shouldHaveDefaultMemoryRetrievalConfig() {
            // When
            val config = AIPipelineConfig()

            // Then
            assertNotNull(config.memoryRetrievalConfig)
        }

        @Test
        @DisplayName("should have default context chaining config")
        fun shouldHaveDefaultContextChainingConfig() {
            // When
            val config = AIPipelineConfig()

            // Then
            assertNotNull(config.contextChainingConfig)
        }
    }

    @Nested
    @DisplayName("AIPipelineConfig Custom Values Tests")
    inner class CustomValuesTests {

        @Test
        @DisplayName("should allow custom maxRetries")
        fun shouldAllowCustomMaxRetries() {
            // When
            val config = AIPipelineConfig(maxRetries = 5)

            // Then
            assertEquals(5, config.maxRetries)
        }

        @Test
        @DisplayName("should allow custom timeout")
        fun shouldAllowCustomTimeout() {
            // When
            val config = AIPipelineConfig(timeoutSeconds = 60)

            // Then
            assertEquals(60, config.timeoutSeconds)
        }

        @Test
        @DisplayName("should allow custom agent priorities")
        fun shouldAllowCustomAgentPriorities() {
            // Given
            val customPriorities = mapOf(
                AgentType.GENESIS to 0.95f,
                AgentType.KAI to 0.85f
            )

            // When
            val config = AIPipelineConfig(agentPriorities = customPriorities)

            // Then
            assertEquals(0.95f, config.agentPriorities[AgentType.GENESIS])
            assertEquals(0.85f, config.agentPriorities[AgentType.KAI])
        }

        @Test
        @DisplayName("should allow zero maxActiveTasks")
        fun shouldAllowZeroMaxActiveTasks() {
            // When
            val config = AIPipelineConfig(maxActiveTasks = 0)

            // Then
            assertEquals(0, config.maxActiveTasks)
        }

        @Test
        @DisplayName("should allow negative timeout for unlimited")
        fun shouldAllowNegativeTimeoutForUnlimited() {
            // When
            val config = AIPipelineConfig(timeoutSeconds = -1)

            // Then
            assertEquals(-1, config.timeoutSeconds)
        }
    }

    @Nested
    @DisplayName("MemoryRetrievalConfig Tests")
    inner class MemoryRetrievalConfigTests {

        @Test
        @DisplayName("should have correct default maxContextLength")
        fun shouldHaveCorrectDefaultMaxContextLength() {
            // When
            val config = MemoryRetrievalConfig()

            // Then
            assertEquals(2000, config.maxContextLength)
        }

        @Test
        @DisplayName("should have correct default similarityThreshold")
        fun shouldHaveCorrectDefaultSimilarityThreshold() {
            // When
            val config = MemoryRetrievalConfig()

            // Then
            assertEquals(0.75f, config.similarityThreshold)
        }

        @Test
        @DisplayName("should have correct default maxRetrievedItems")
        fun shouldHaveCorrectDefaultMaxRetrievedItems() {
            // When
            val config = MemoryRetrievalConfig()

            // Then
            assertEquals(5, config.maxRetrievedItems)
        }

        @Test
        @DisplayName("should allow custom maxContextLength")
        fun shouldAllowCustomMaxContextLength() {
            // When
            val config = MemoryRetrievalConfig(maxContextLength = 5000)

            // Then
            assertEquals(5000, config.maxContextLength)
        }

        @Test
        @DisplayName("should allow custom similarityThreshold")
        fun shouldAllowCustomSimilarityThreshold() {
            // When
            val config = MemoryRetrievalConfig(similarityThreshold = 0.9f)

            // Then
            assertEquals(0.9f, config.similarityThreshold)
        }

        @Test
        @DisplayName("should allow custom maxRetrievedItems")
        fun shouldAllowCustomMaxRetrievedItems() {
            // When
            val config = MemoryRetrievalConfig(maxRetrievedItems = 10)

            // Then
            assertEquals(10, config.maxRetrievedItems)
        }
    }

    @Nested
    @DisplayName("ContextChainingConfig Tests")
    inner class ContextChainingConfigTests {

        @Test
        @DisplayName("should have correct default maxChainLength")
        fun shouldHaveCorrectDefaultMaxChainLength() {
            // When
            val config = ContextChainingConfig()

            // Then
            assertEquals(10, config.maxChainLength)
        }

        @Test
        @DisplayName("should have correct default relevanceThreshold")
        fun shouldHaveCorrectDefaultRelevanceThreshold() {
            // When
            val config = ContextChainingConfig()

            // Then
            assertEquals(0.6f, config.relevanceThreshold)
        }

        @Test
        @DisplayName("should have correct default decayRate")
        fun shouldHaveCorrectDefaultDecayRate() {
            // When
            val config = ContextChainingConfig()

            // Then
            assertEquals(0.9f, config.decayRate)
        }

        @Test
        @DisplayName("should allow custom maxChainLength")
        fun shouldAllowCustomMaxChainLength() {
            // When
            val config = ContextChainingConfig(maxChainLength = 20)

            // Then
            assertEquals(20, config.maxChainLength)
        }

        @Test
        @DisplayName("should allow custom relevanceThreshold")
        fun shouldAllowCustomRelevanceThreshold() {
            // When
            val config = ContextChainingConfig(relevanceThreshold = 0.8f)

            // Then
            assertEquals(0.8f, config.relevanceThreshold)
        }

        @Test
        @DisplayName("should allow custom decayRate")
        fun shouldAllowCustomDecayRate() {
            // When
            val config = ContextChainingConfig(decayRate = 0.95f)

            // Then
            assertEquals(0.95f, config.decayRate)
        }
    }

    @Nested
    @DisplayName("Data Class Equality Tests")
    inner class EqualityTests {

        @Test
        @DisplayName("should equal same config")
        fun shouldEqualSameConfig() {
            // Given
            val config1 = AIPipelineConfig()
            val config2 = AIPipelineConfig()

            // Then
            assertEquals(config1, config2)
        }

        @Test
        @DisplayName("should not equal different config")
        fun shouldNotEqualDifferentConfig() {
            // Given
            val config1 = AIPipelineConfig(maxRetries = 3)
            val config2 = AIPipelineConfig(maxRetries = 5)

            // Then
            assertNotEquals(config1, config2)
        }

        @Test
        @DisplayName("should equal same memory config")
        fun shouldEqualSameMemoryConfig() {
            // Given
            val config1 = MemoryRetrievalConfig()
            val config2 = MemoryRetrievalConfig()

            // Then
            assertEquals(config1, config2)
        }

        @Test
        @DisplayName("should equal same context chaining config")
        fun shouldEqualSameContextChainingConfig() {
            // Given
            val config1 = ContextChainingConfig()
            val config2 = ContextChainingConfig()

            // Then
            assertEquals(config1, config2)
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    inner class ValidationTests {

        @Test
        @DisplayName("should validate priority threshold bounds")
        fun shouldValidatePriorityThresholdBounds() {
            // When
            val config = AIPipelineConfig(priorityThreshold = 0.7f)

            // Then
            assertTrue(config.priorityThreshold >= 0.0f)
            assertTrue(config.priorityThreshold <= 1.0f)
        }

        @Test
        @DisplayName("should validate weight sum")
        fun shouldValidateWeightSum() {
            // When
            val config = AIPipelineConfig()

            // Then
            val weightSum = config.priorityWeight + config.urgencyWeight + config.importanceWeight
            assertEquals(1.0f, weightSum, 0.01f)
        }

        @Test
        @DisplayName("should validate similarity threshold bounds")
        fun shouldValidateSimilarityThresholdBounds() {
            // When
            val config = MemoryRetrievalConfig()

            // Then
            assertTrue(config.similarityThreshold >= 0.0f)
            assertTrue(config.similarityThreshold <= 1.0f)
        }

        @Test
        @DisplayName("should validate decay rate bounds")
        fun shouldValidateDecayRateBounds() {
            // When
            val config = ContextChainingConfig()

            // Then
            assertTrue(config.decayRate >= 0.0f)
            assertTrue(config.decayRate <= 1.0f)
        }
    }
}