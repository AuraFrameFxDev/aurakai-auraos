package dev.aurakai.auraframefx.ai.pipeline

import dev.aurakai.auraframefx.model.AgentType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("AIPipelineConfig Tests")
class AIPipelineConfigTest {

    @Nested
    @DisplayName("Default Configuration")
    inner class DefaultConfigurationTests {

        @Test
        @DisplayName("Should create config with default values")
        fun shouldCreateConfigWithDefaults() {
            val config = AIPipelineConfig()

            assertEquals(3, config.maxRetries)
            assertEquals(30, config.timeoutSeconds)
            assertEquals(5, config.contextWindowSize)
            assertEquals(0.7f, config.priorityThreshold, 0.001f)
            assertEquals(0.4f, config.priorityWeight, 0.001f)
            assertEquals(0.4f, config.urgencyWeight, 0.001f)
            assertEquals(0.2f, config.importanceWeight, 0.001f)
            assertEquals(10, config.maxActiveTasks)
            assertNotNull(config.memoryRetrievalConfig)
            assertNotNull(config.contextChainingConfig)
        }

        @Test
        @DisplayName("Should have correct default agent priorities")
        fun shouldHaveCorrectDefaultAgentPriorities() {
            val config = AIPipelineConfig()

            assertEquals(1.0f, config.agentPriorities[AgentType.GENESIS], 0.001f)
            assertEquals(0.9f, config.agentPriorities[AgentType.KAI], 0.001f)
            assertEquals(0.8f, config.agentPriorities[AgentType.AURA], 0.001f)
            assertEquals(0.7f, config.agentPriorities[AgentType.CASCADE], 0.001f)
        }

        @Test
        @DisplayName("Weight sum should equal 1.0 for valid priority calculation")
        fun weightSumShouldBeValid() {
            val config = AIPipelineConfig()
            val weightSum = config.priorityWeight + config.urgencyWeight + config.importanceWeight

            assertEquals(1.0f, weightSum, 0.001f)
        }
    }

    @Nested
    @DisplayName("Custom Configuration")
    inner class CustomConfigurationTests {

        @Test
        @DisplayName("Should allow custom max retries")
        fun shouldAllowCustomMaxRetries() {
            val config = AIPipelineConfig(maxRetries = 5)
            assertEquals(5, config.maxRetries)
        }

        @Test
        @DisplayName("Should allow custom timeout")
        fun shouldAllowCustomTimeout() {
            val config = AIPipelineConfig(timeoutSeconds = 60)
            assertEquals(60, config.timeoutSeconds)
        }

        @Test
        @DisplayName("Should allow custom context window size")
        fun shouldAllowCustomContextWindowSize() {
            val config = AIPipelineConfig(contextWindowSize = 10)
            assertEquals(10, config.contextWindowSize)
        }

        @Test
        @DisplayName("Should allow custom weights")
        fun shouldAllowCustomWeights() {
            val config = AIPipelineConfig(
                priorityWeight = 0.5f,
                urgencyWeight = 0.3f,
                importanceWeight = 0.2f
            )

            assertEquals(0.5f, config.priorityWeight, 0.001f)
            assertEquals(0.3f, config.urgencyWeight, 0.001f)
            assertEquals(0.2f, config.importanceWeight, 0.001f)
        }

        @Test
        @DisplayName("Should allow custom agent priorities")
        fun shouldAllowCustomAgentPriorities() {
            val customPriorities = mapOf(
                AgentType.GENESIS to 0.9f,
                AgentType.KAI to 1.0f
            )
            val config = AIPipelineConfig(agentPriorities = customPriorities)

            assertEquals(customPriorities, config.agentPriorities)
        }
    }

    @Nested
    @DisplayName("MemoryRetrievalConfig Tests")
    inner class MemoryRetrievalConfigTests {

        @Test
        @DisplayName("Should create memory config with defaults")
        fun shouldCreateMemoryConfigWithDefaults() {
            val memoryConfig = MemoryRetrievalConfig()

            assertEquals(2000, memoryConfig.maxContextLength)
            assertEquals(0.75f, memoryConfig.similarityThreshold, 0.001f)
            assertEquals(5, memoryConfig.maxRetrievedItems)
        }

        @Test
        @DisplayName("Should allow custom memory config")
        fun shouldAllowCustomMemoryConfig() {
            val memoryConfig = MemoryRetrievalConfig(
                maxContextLength = 3000,
                similarityThreshold = 0.8f,
                maxRetrievedItems = 10
            )

            assertEquals(3000, memoryConfig.maxContextLength)
            assertEquals(0.8f, memoryConfig.similarityThreshold, 0.001f)
            assertEquals(10, memoryConfig.maxRetrievedItems)
        }
    }

    @Nested
    @DisplayName("ContextChainingConfig Tests")
    inner class ContextChainingConfigTests {

        @Test
        @DisplayName("Should create context chaining config with defaults")
        fun shouldCreateContextChainingConfigWithDefaults() {
            val chainingConfig = ContextChainingConfig()

            assertEquals(10, chainingConfig.maxChainLength)
            assertEquals(0.6f, chainingConfig.relevanceThreshold, 0.001f)
            assertEquals(0.9f, chainingConfig.decayRate, 0.001f)
        }

        @Test
        @DisplayName("Decay rate should be between 0 and 1")
        fun decayRateShouldBeValid() {
            val chainingConfig = ContextChainingConfig()

            assertTrue(chainingConfig.decayRate >= 0.0f)
            assertTrue(chainingConfig.decayRate <= 1.0f)
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    inner class EdgeCasesTests {

        @Test
        @DisplayName("Should handle zero max retries")
        fun shouldHandleZeroMaxRetries() {
            val config = AIPipelineConfig(maxRetries = 0)
            assertEquals(0, config.maxRetries)
        }

        @Test
        @DisplayName("Should handle empty agent priorities map")
        fun shouldHandleEmptyAgentPriorities() {
            val config = AIPipelineConfig(agentPriorities = emptyMap())
            assertTrue(config.agentPriorities.isEmpty())
        }

        @Test
        @DisplayName("Should handle boundary priority threshold values")
        fun shouldHandleBoundaryPriorityThreshold() {
            val config1 = AIPipelineConfig(priorityThreshold = 0.0f)
            val config2 = AIPipelineConfig(priorityThreshold = 1.0f)

            assertEquals(0.0f, config1.priorityThreshold, 0.001f)
            assertEquals(1.0f, config2.priorityThreshold, 0.001f)
        }
    }

    @Nested
    @DisplayName("Data Class Behavior")
    inner class DataClassBehaviorTests {

        @Test
        @DisplayName("Should support copy with modified values")
        fun shouldSupportCopyWithModifiedValues() {
            val original = AIPipelineConfig(maxRetries = 3)
            val copied = original.copy(maxRetries = 5)

            assertEquals(3, original.maxRetries)
            assertEquals(5, copied.maxRetries)
        }

        @Test
        @DisplayName("Should have proper equals implementation")
        fun shouldHaveProperEquals() {
            val config1 = AIPipelineConfig(maxRetries = 3, timeoutSeconds = 30)
            val config2 = AIPipelineConfig(maxRetries = 3, timeoutSeconds = 30)
            val config3 = AIPipelineConfig(maxRetries = 5, timeoutSeconds = 30)

            assertEquals(config1, config2)
            assertNotEquals(config1, config3)
        }

        @Test
        @DisplayName("Should have proper hashCode implementation")
        fun shouldHaveProperHashCode() {
            val config1 = AIPipelineConfig(maxRetries = 3)
            val config2 = AIPipelineConfig(maxRetries = 3)

            assertEquals(config1.hashCode(), config2.hashCode())
        }
    }
}