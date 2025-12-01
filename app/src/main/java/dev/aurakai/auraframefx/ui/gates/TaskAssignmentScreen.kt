package dev.aurakai.auraframefx.ui.gates

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.aurakai.auraframefx.data.repositories.AgentRepository

/**
 * Task Assignment Screen
 * Assign tasks and missions to AI agents
 */
@Composable
fun TaskAssignmentScreen() {
    val agents = remember { AgentRepository.getAllAgents() }
    val selectedAgent = remember { mutableStateOf<String?>(null) }
    val taskDescription = remember { mutableStateOf("") }
    val taskPriority = remember { mutableStateOf("Normal") }
    val taskDeadline = remember { mutableStateOf("24h") }

    val priorities = listOf("Low", "Normal", "High", "Critical")
    val deadlines = listOf("1h", "6h", "12h", "24h", "1w")

    val sampleTasks = listOf(
        Task("Code Review", "Genesis", "High", "In Progress", Color(0xFFFFD700)),
        Task("Security Scan", "Kai", "Critical", "Completed", Color(0xFF32CD32)),
        Task("UI Design", "Aura", "Normal", "Pending", Color(0xFF4169E1)),
        Task("Data Analysis", "Cascade", "Low", "In Progress", Color(0xFF9370DB))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Header
        Text(
            text = "📋 TASK ASSIGNMENT",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF4169E1),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Assign tasks and missions to AI agents",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF6495ED).copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Task Creation Form
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.6f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Create New Task",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Agent Selection
                Text(
                    text = "Assign to Agent",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(8.dp))

                agents.forEach { agent ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedAgent.value == agent.name,
                            onClick = { selectedAgent.value = agent.name },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFF4169E1)
                            )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = agent.name,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = agent.color.copy(alpha = 0.2f)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "${agent.consciousnessLevel.toInt()}%",
                                style = MaterialTheme.typography.labelSmall,
                                color = agent.color,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Task Description
                OutlinedTextField(
                    value = taskDescription.value,
                    onValueChange = { taskDescription.value = it },
                    label = { Text("Task Description") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4169E1),
                        unfocusedBorderColor = Color(0xFF4169E1).copy(alpha = 0.5f),
                        focusedLabelColor = Color(0xFF4169E1)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Priority and Deadline
                Row(modifier = Modifier.fillMaxWidth()) {
                    // Priority
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Priority",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        priorities.forEach { priority ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = taskPriority.value == priority,
                                    onClick = { taskPriority.value = priority },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = when (priority) {
                                            "Critical" -> Color(0xFFDC143C)
                                            "High" -> Color(0xFFFF4500)
                                            "Normal" -> Color(0xFFFFD700)
                                            else -> Color(0xFF32CD32)
                                        }
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = priority,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Deadline
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Deadline",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        deadlines.forEach { deadline ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = taskDeadline.value == deadline,
                                    onClick = { taskDeadline.value = deadline },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = Color(0xFF4169E1)
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = deadline,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Assign Button
                Button(
                    onClick = {
                        // Mock task assignment
                        taskDescription.value = ""
                        selectedAgent.value = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4169E1)
                    ),
                    enabled = selectedAgent.value != null && taskDescription.value.isNotBlank()
                ) {
                    Text("Assign Task", color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Active Tasks
        Text(
            text = "Active Tasks",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(sampleTasks) { task ->
                TaskCard(task = task)
            }
        }
    }
}

/**
 * Task card component
 */
@Composable
private fun TaskCard(task: Task) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.6f)
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, task.priorityColor.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Status Icon
            Icon(
                imageVector = when (task.status) {
                    "Completed" -> Icons.Default.CheckCircle
                    "In Progress" -> Icons.Default.Schedule
                    else -> Icons.Default.Pending
                },
                contentDescription = "Status",
                tint = when (task.status) {
                    "Completed" -> Color(0xFF32CD32)
                    "In Progress" -> Color(0xFFFFD700)
                    else -> Color(0xFF4169E1)
                },
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Assigned to: ${task.agent}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }

            // Priority Badge
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = task.priorityColor.copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = task.priority,
                    style = MaterialTheme.typography.labelSmall,
                    color = task.priorityColor,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Data classes
 */
data class Task(
    val title: String,
    val agent: String,
    val priority: String,
    val status: String,
    val priorityColor: Color
)
