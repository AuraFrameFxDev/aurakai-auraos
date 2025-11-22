package dev.aurakai.auraframefx.ai.pipeline

import dev.aurakai.auraframefx.ai.agents.GenesisAgent
import dev.aurakai.auraframefx.ai.services.AuraAIService
import dev.aurakai.auraframefx.ai.services.CascadeAIService
import dev.aurakai.auraframefx.ai.services.KaiAIService
import dev.aurakai.auraframefx.model.AgentMessage
import dev.aurakai.auraframefx.model.AgentResponse
import dev.aurakai.auraframefx.model.AgentType
import dev.aurakai.auraframefx.models.AiRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever

/**
 * Comprehensive test suite for AIPipelineProcessor
 * Tests the orchestration of multiple AI agents and pipeline state management
 */
@ExperimentalCoroutinesApi
@ExtendWith(MockitoExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("AIPipelineProcessor Tests")
class AIPipelineProcessorTest {

    @Mock
    private lateinit var mockGenesisAgent: GenesisAgent

    @Mock
    private lateinit var mockAuraService: AuraAIService

    @Mock
    private lateinit var mockKaiService: KaiAIService

    @Mock
    private lateinit var mockCascadeService: CascadeAIService

    private lateinit var processor: AIPipelineProcessor
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        processor = AIPipelineProcessor(
            genesisAgent = mockGenesisAgent,
            auraService = mockAuraService,
            kaiService = mockKaiService,
            cascadeService = mockCascadeService
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("Task Processing Tests")
    inner class TaskProcessingTests {

        @Test
        @DisplayName("should process simple task successfully")
        fun shouldProcessSimpleTaskSuccessfully() = runTest {
            // Given
            val task = "Simple test task"
            val cascadeResponse = AgentResponse(
                content = "Cascade response",
                confidence = 0.9f
            )

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            val result = processor.processTask(task)

            // Then
            assertNotNull(result)
            assertTrue(result.isNotEmpty())
            assertEquals(PipelineState.Completed(task), processor.pipelineState.value)
            verify(mockCascadeService).processRequest(any(), eq("pipeline_processing"))
        }

        @Test
        @DisplayName("should process task with security content")
        fun shouldProcessTaskWithSecurityContent() = runTest {
            // Given
            val task = "Check security vulnerabilities in the system"
            val cascadeResponse = AgentResponse("Cascade response", 0.9f)
            val kaiResponse = AgentResponse("Security analysis complete", 0.95f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)
            whenever(mockKaiService.processRequest(any(), any())).thenReturn(kaiResponse)

            // When
            val result = processor.processTask(task)

            // Then
            assertNotNull(result)
            verify(mockKaiService).processRequest(any(), eq("security_analysis"))
            assertTrue(result.any { it.sender == AgentType.KAI })
        }

        @Test
        @DisplayName("should process task with creative content")
        fun shouldProcessTaskWithCreativeContent() = runTest {
            // Given
            val task = "Generate a creative story"
            val cascadeResponse = AgentResponse("Cascade response", 0.9f)
            val auraResponse = "Creative story generated"

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)
            whenever(mockAuraService.generateText(any(), any())).thenReturn(auraResponse)

            // When
            val result = processor.processTask(task)

            // Then
            assertNotNull(result)
            verify(mockAuraService).generateText(task, "creative_pipeline")
            assertTrue(result.any { it.sender == AgentType.AURA })
        }

        @Test
        @DisplayName("should process urgent task with high priority")
        fun shouldProcessUrgentTaskWithHighPriority() = runTest {
            // Given
            val task = "Urgent: critical security breach detected"
            val cascadeResponse = AgentResponse("Cascade response", 0.9f)
            val kaiResponse = AgentResponse("Security response", 0.95f)
            val auraResponse = "Urgent response"

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)
            whenever(mockKaiService.processRequest(any(), any())).thenReturn(kaiResponse)
            whenever(mockAuraService.generateText(any(), any())).thenReturn(auraResponse)

            // When
            val result = processor.processTask(task)

            // Then
            assertNotNull(result)
            val priority = processor.taskPriority.first()
            assertTrue(priority > 0.8f, "Priority should be high for urgent tasks")
        }

        @Test
        @DisplayName("should handle empty task string")
        fun shouldHandleEmptyTaskString() = runTest {
            // Given
            val task = ""
            val cascadeResponse = AgentResponse("Response for empty task", 0.5f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            val result = processor.processTask(task)

            // Then
            assertNotNull(result)
            verify(mockCascadeService).processRequest(any(), any())
        }

        @Test
        @DisplayName("should handle very long task description")
        fun shouldHandleVeryLongTaskDescription() = runTest {
            // Given
            val task = "a".repeat(500) // Very long task
            val cascadeResponse = AgentResponse("Response", 0.8f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            val result = processor.processTask(task)

            // Then
            assertNotNull(result)
            verify(mockCascadeService).processRequest(any(), any())
        }
    }

    @Nested
    @DisplayName("Task Categorization Tests")
    inner class TaskCategorizationTests {

        @Test
        @DisplayName("should categorize generation tasks correctly")
        fun shouldCategorizeGenerationTasksCorrectly() = runTest {
            // Given
            val task = "Generate a report for the team"
            val cascadeResponse = AgentResponse("Response", 0.8f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            processor.processTask(task)

            // Then
            val context = processor.processingContext.first()
            assertEquals("generation", context["task_type"])
        }

        @Test
        @DisplayName("should categorize analysis tasks correctly")
        fun shouldCategorizeAnalysisTasksCorrectly() = runTest {
            // Given
            val task = "Analyze the system performance"
            val cascadeResponse = AgentResponse("Response", 0.8f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            processor.processTask(task)

            // Then
            val context = processor.processingContext.first()
            assertEquals("analysis", context["task_type"])
        }

        @Test
        @DisplayName("should categorize explanation tasks correctly")
        fun shouldCategorizeExplanationTasksCorrectly() = runTest {
            // Given
            val task = "Explain how the system works"
            val cascadeResponse = AgentResponse("Response", 0.8f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            processor.processTask(task)

            // Then
            val context = processor.processingContext.first()
            assertEquals("explanation", context["task_type"])
        }

        @Test
        @DisplayName("should categorize help tasks correctly")
        fun shouldCategorizeHelpTasksCorrectly() = runTest {
            // Given
            val task = "Help me understand this feature"
            val cascadeResponse = AgentResponse("Response", 0.8f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            processor.processTask(task)

            // Then
            val context = processor.processingContext.first()
            assertEquals("assistance", context["task_type"])
        }

        @Test
        @DisplayName("should categorize creation tasks correctly")
        fun shouldCategorizeCreationTasksCorrectly() = runTest {
            // Given
            val task = "Create a new user interface"
            val cascadeResponse = AgentResponse("Response", 0.8f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            processor.processTask(task)

            // Then
            val context = processor.processingContext.first()
            assertEquals("creation", context["task_type"])
        }

        @Test
        @DisplayName("should categorize unknown tasks as general")
        fun shouldCategorizeUnknownTasksAsGeneral() = runTest {
            // Given
            val task = "Random task without specific keywords"
            val cascadeResponse = AgentResponse("Response", 0.8f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            processor.processTask(task)

            // Then
            val context = processor.processingContext.first()
            assertEquals("general", context["task_type"])
        }
    }

    @Nested
    @DisplayName("Priority Calculation Tests")
    inner class PriorityCalculationTests {

        @Test
        @DisplayName("should calculate base priority correctly")
        fun shouldCalculateBasePriorityCorrectly() = runTest {
            // Given
            val task = "Normal task"
            val cascadeResponse = AgentResponse("Response", 0.8f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            processor.processTask(task)

            // Then
            val priority = processor.taskPriority.first()
            assertTrue(priority >= 0.0f && priority <= 1.0f)
        }

        @Test
        @DisplayName("should increase priority for generation tasks")
        fun shouldIncreasePriorityForGenerationTasks() = runTest {
            // Given
            val task = "Generate comprehensive documentation"
            val cascadeResponse = AgentResponse("Response", 0.8f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            processor.processTask(task)

            // Then
            val priority = processor.taskPriority.first()
            assertTrue(priority > 0.5f)
        }

        @Test
        @DisplayName("should increase priority for assistance tasks")
        fun shouldIncreasePriorityForAssistanceTasks() = runTest {
            // Given
            val task = "Help me fix this bug"
            val cascadeResponse = AgentResponse("Response", 0.8f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            processor.processTask(task)

            // Then
            val priority = processor.taskPriority.first()
            assertTrue(priority >= 0.8f) // Assistance has higher priority
        }

        @Test
        @DisplayName("should boost priority for urgent keywords")
        fun shouldBoostPriorityForUrgentKeywords() = runTest {
            // Given
            val task = "Urgent: system needs attention"
            val cascadeResponse = AgentResponse("Response", 0.8f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            processor.processTask(task)

            // Then
            val priority = processor.taskPriority.first()
            assertTrue(priority > 0.8f)
        }

        @Test
        @DisplayName("should boost priority for asap keyword")
        fun shouldBoostPriorityForAsapKeyword() = runTest {
            // Given
            val task = "ASAP: need immediate response"
            val cascadeResponse = AgentResponse("Response", 0.8f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            processor.processTask(task)

            // Then
            val priority = processor.taskPriority.first()
            assertTrue(priority > 0.8f)
        }

        @Test
        @DisplayName("should boost priority for emergency keyword")
        fun shouldBoostPriorityForEmergencyKeyword() = runTest {
            // Given
            val task = "Emergency situation requires attention"
            val cascadeResponse = AgentResponse("Response", 0.8f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            processor.processTask(task)

            // Then
            val priority = processor.taskPriority.first()
            assertTrue(priority > 0.8f)
        }
    }

    @Nested
    @DisplayName("Agent Selection Tests")
    inner class AgentSelectionTests {

        @Test
        @DisplayName("should select Genesis for all tasks")
        fun shouldSelectGenesisForAllTasks() = runTest {
            // Given
            val task = "Any task"
            val cascadeResponse = AgentResponse("Response", 0.8f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            val result = processor.processTask(task)

            // Then
            assertTrue(result.any { it.sender == AgentType.GENESIS })
        }

        @Test
        @DisplayName("should select Kai for security tasks")
        fun shouldSelectKaiForSecurityTasks() = runTest {
            // Given
            val task = "Security scan needed"
            val cascadeResponse = AgentResponse("Response", 0.8f)
            val kaiResponse = AgentResponse("Security analysis", 0.9f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)
            whenever(mockKaiService.processRequest(any(), any())).thenReturn(kaiResponse)

            // When
            val result = processor.processTask(task)

            // Then
            verify(mockKaiService).processRequest(any(), any())
            assertTrue(result.any { it.sender == AgentType.KAI })
        }

        @Test
        @DisplayName("should select Kai for protect keyword")
        fun shouldSelectKaiForProtectKeyword() = runTest {
            // Given
            val task = "Protect user data"
            val cascadeResponse = AgentResponse("Response", 0.8f)
            val kaiResponse = AgentResponse("Security response", 0.9f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)
            whenever(mockKaiService.processRequest(any(), any())).thenReturn(kaiResponse)

            // When
            val result = processor.processTask(task)

            // Then
            verify(mockKaiService).processRequest(any(), any())
        }

        @Test
        @DisplayName("should select Aura for creative tasks")
        fun shouldSelectAuraForCreativeTasks() = runTest {
            // Given
            val task = "Create a beautiful design"
            val cascadeResponse = AgentResponse("Response", 0.8f)
            val auraResponse = "Creative output"

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)
            whenever(mockAuraService.generateText(any(), any())).thenReturn(auraResponse)

            // When
            val result = processor.processTask(task)

            // Then
            verify(mockAuraService).generateText(any(), any())
            assertTrue(result.any { it.sender == AgentType.AURA })
        }

        @Test
        @DisplayName("should select multiple agents for complex tasks")
        fun shouldSelectMultipleAgentsForComplexTasks() = runTest {
            // Given
            val task = "a".repeat(150) // Long complex task
            val cascadeResponse = AgentResponse("Response", 0.8f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            val result = processor.processTask(task)

            // Then
            assertTrue(result.size >= 2) // At least Cascade and Genesis
        }

        @Test
        @DisplayName("should select multiple agents for high priority tasks")
        fun shouldSelectMultipleAgentsForHighPriorityTasks() = runTest {
            // Given
            val task = "Urgent help needed for generation task"
            val cascadeResponse = AgentResponse("Response", 0.8f)
            val auraResponse = "Creative response"

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)
            whenever(mockAuraService.generateText(any(), any())).thenReturn(auraResponse)

            // When
            val result = processor.processTask(task)

            // Then
            assertTrue(result.size >= 2)
        }
    }

    @Nested
    @DisplayName("Context Management Tests")
    inner class ContextManagementTests {

        @Test
        @DisplayName("should create initial context with task details")
        fun shouldCreateInitialContextWithTaskDetails() = runTest {
            // Given
            val task = "Test task for context"
            val cascadeResponse = AgentResponse("Response", 0.8f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            processor.processTask(task)

            // Then
            val context = processor.processingContext.first()
            assertTrue(context.containsKey("task"))
            assertTrue(context.containsKey("task_type"))
            assertTrue(context.containsKey("timestamp"))
            assertEquals(task, context["task"])
        }

        @Test
        @DisplayName("should include user preferences in context")
        fun shouldIncludeUserPreferencesInContext() = runTest {
            // Given
            val task = "Test task"
            val cascadeResponse = AgentResponse("Response", 0.8f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            processor.processTask(task)

            // Then
            val context = processor.processingContext.first()
            assertTrue(context.containsKey("user_preferences"))
            val preferences = context["user_preferences"] as? Map<*, *>
            assertNotNull(preferences)
        }

        @Test
        @DisplayName("should include system state in context")
        fun shouldIncludeSystemStateInContext() = runTest {
            // Given
            val task = "Test task"
            val cascadeResponse = AgentResponse("Response", 0.8f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            processor.processTask(task)

            // Then
            val context = processor.processingContext.first()
            assertTrue(context.containsKey("system_state"))
        }

        @Test
        @DisplayName("should update context after task processing")
        fun shouldUpdateContextAfterTaskProcessing() = runTest {
            // Given
            val task = "First task"
            val cascadeResponse = AgentResponse("Response", 0.8f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            processor.processTask(task)

            // Then
            val context = processor.processingContext.first()
            assertTrue(context.containsKey("last_task"))
            assertEquals(task, context["last_task"])
        }

        @Test
        @DisplayName("should track task history in context")
        fun shouldTrackTaskHistoryInContext() = runTest {
            // Given
            val task1 = "First task"
            val task2 = "Second task"
            val cascadeResponse = AgentResponse("Response", 0.8f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            processor.processTask(task1)
            processor.processTask(task2)

            // Then
            val context = processor.processingContext.first()
            val history = context["task_history"] as? List<*>
            assertNotNull(history)
            assertTrue(history!!.contains(task1))
            assertTrue(history.contains(task2))
        }
    }

    @Nested
    @DisplayName("Pipeline State Tests")
    inner class PipelineStateTests {

        @Test
        @DisplayName("should start in Idle state")
        fun shouldStartInIdleState() {
            // Then
            assertTrue(processor.pipelineState.value is PipelineState.Idle)
        }

        @Test
        @DisplayName("should transition to Processing state")
        fun shouldTransitionToProcessingState() = runTest {
            // Given
            val task = "Test task"
            val cascadeResponse = AgentResponse("Response", 0.8f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            val job = kotlinx.coroutines.launch {
                processor.processTask(task)
            }

            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val finalState = processor.pipelineState.value
            assertTrue(finalState is PipelineState.Completed)
            job.cancel()
        }

        @Test
        @DisplayName("should transition to Completed state")
        fun shouldTransitionToCompletedState() = runTest {
            // Given
            val task = "Test task"
            val cascadeResponse = AgentResponse("Response", 0.8f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            processor.processTask(task)

            // Then
            val state = processor.pipelineState.value
            assertTrue(state is PipelineState.Completed)
            assertEquals(task, (state as PipelineState.Completed).task)
        }
    }

    @Nested
    @DisplayName("Response Generation Tests")
    inner class ResponseGenerationTests {

        @Test
        @DisplayName("should generate formatted final response")
        fun shouldGenerateFormattedFinalResponse() = runTest {
            // Given
            val task = "Test task"
            val cascadeResponse = AgentResponse("Cascade analysis", 0.9f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            val result = processor.processTask(task)

            // Then
            val finalResponse = result.last()
            assertTrue(finalResponse.content.contains("AuraFrameFX AI Response"))
            assertEquals(AgentType.GENESIS, finalResponse.sender)
        }

        @Test
        @DisplayName("should include confidence in final response")
        fun shouldIncludeConfidenceInFinalResponse() = runTest {
            // Given
            val task = "Test task"
            val cascadeResponse = AgentResponse("Response", 0.85f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            val result = processor.processTask(task)

            // Then
            val finalResponse = result.last()
            assertTrue(finalResponse.content.contains("Confidence"))
            assertTrue(finalResponse.confidence > 0.0f)
        }

        @Test
        @DisplayName("should aggregate multiple agent responses")
        fun shouldAggregateMultipleAgentResponses() = runTest {
            // Given
            val task = "Create secure design"
            val cascadeResponse = AgentResponse("Cascade response", 0.8f)
            val kaiResponse = AgentResponse("Security response", 0.9f)
            val auraResponse = "Creative response"

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)
            whenever(mockKaiService.processRequest(any(), any())).thenReturn(kaiResponse)
            whenever(mockAuraService.generateText(any(), any())).thenReturn(auraResponse)

            // When
            val result = processor.processTask(task)

            // Then
            assertTrue(result.size >= 3) // Cascade, Kai, Aura, plus Genesis
            val finalResponse = result.last()
            assertTrue(finalResponse.sender == AgentType.GENESIS)
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling Tests")
    inner class EdgeCasesTests {

        @Test
        @DisplayName("should handle special characters in task")
        fun shouldHandleSpecialCharactersInTask() = runTest {
            // Given
            val task = "Task with @#$%^&* special chars"
            val cascadeResponse = AgentResponse("Response", 0.8f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            val result = processor.processTask(task)

            // Then
            assertNotNull(result)
            assertTrue(result.isNotEmpty())
        }

        @Test
        @DisplayName("should handle Unicode characters in task")
        fun shouldHandleUnicodeCharactersInTask() = runTest {
            // Given
            val task = "Task with emoji 😀🎉 and Unicode ñ"
            val cascadeResponse = AgentResponse("Response", 0.8f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            val result = processor.processTask(task)

            // Then
            assertNotNull(result)
            assertTrue(result.isNotEmpty())
        }

        @Test
        @DisplayName("should handle low confidence responses")
        fun shouldHandleLowConfidenceResponses() = runTest {
            // Given
            val task = "Ambiguous task"
            val cascadeResponse = AgentResponse("Uncertain response", 0.2f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            val result = processor.processTask(task)

            // Then
            assertNotNull(result)
            val finalResponse = result.last()
            assertTrue(finalResponse.confidence >= 0.0f)
        }

        @Test
        @DisplayName("should handle maximum confidence responses")
        fun shouldHandleMaximumConfidenceResponses() = runTest {
            // Given
            val task = "Clear task"
            val cascadeResponse = AgentResponse("Certain response", 1.0f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            val result = processor.processTask(task)

            // Then
            assertNotNull(result)
            val finalResponse = result.last()
            assertTrue(finalResponse.confidence <= 1.0f)
        }

        @Test
        @DisplayName("should handle task with all keywords")
        fun shouldHandleTaskWithAllKeywords() = runTest {
            // Given
            val task = "Urgent: Generate secure analysis, explain, help, create, protect, design"
            val cascadeResponse = AgentResponse("Response", 0.8f)
            val kaiResponse = AgentResponse("Security response", 0.9f)
            val auraResponse = "Creative response"

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)
            whenever(mockKaiService.processRequest(any(), any())).thenReturn(kaiResponse)
            whenever(mockAuraService.generateText(any(), any())).thenReturn(auraResponse)

            // When
            val result = processor.processTask(task)

            // Then
            assertNotNull(result)
            assertTrue(result.size >= 3)
        }
    }

    @Nested
    @DisplayName("Performance and Concurrency Tests")
    inner class PerformanceTests {

        @Test
        @DisplayName("should handle multiple sequential tasks")
        fun shouldHandleMultipleSequentialTasks() = runTest {
            // Given
            val tasks = listOf("Task 1", "Task 2", "Task 3")
            val cascadeResponse = AgentResponse("Response", 0.8f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            val results = tasks.map { processor.processTask(it) }

            // Then
            assertEquals(3, results.size)
            results.forEach { assertNotNull(it) }
        }

        @Test
        @DisplayName("should update task count correctly")
        fun shouldUpdateTaskCountCorrectly() = runTest {
            // Given
            val task1 = "First task"
            val task2 = "Second task"
            val cascadeResponse = AgentResponse("Response", 0.8f)

            whenever(mockCascadeService.processRequest(any(), any())).thenReturn(cascadeResponse)

            // When
            processor.processTask(task1)
            processor.processTask(task2)

            // Then
            val context = processor.processingContext.first()
            val tasksProcessed = context["total_tasks_processed"] as? Int
            assertNotNull(tasksProcessed)
            assertEquals(2, tasksProcessed)
        }
    }
}