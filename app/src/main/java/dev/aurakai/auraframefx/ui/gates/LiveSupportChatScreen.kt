package dev.aurakai.auraframefx.ui.gates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Live Support Chat Screen
 * Real-time assistance from support agents
 */
@Composable
fun LiveSupportChatScreen() {
    val chatMessages = remember { mutableStateListOf<SupportMessage>() }
    val currentMessage = remember { mutableStateOf("") }
    val isTyping = remember { mutableStateOf(false) }
    val supportAgent = remember { mutableStateOf("Alex") }
    val coroutineScope = rememberCoroutineScope()

    // Initialize with welcome message
    LaunchedEffect(Unit) {
        chatMessages.add(
            SupportMessage(
                "Hello! I'm ${supportAgent.value}, your support assistant. How can I help you today?",
                supportAgent.value,
                false,
                "10:30 AM"
            )
        )
    }

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
            text = "💬 LIVE SUPPORT",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF32CD32),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Real-time assistance from support agents",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF90EE90).copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Support Agent Status
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.6f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Agent Avatar
                Card(
                    modifier = Modifier.size(40.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF32CD32).copy(alpha = 0.3f)
                    ),
                    shape = CircleShape
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = supportAgent.value.first().toString(),
                            style = MaterialTheme.typography.titleSmall,
                            color = Color(0xFF32CD32),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = supportAgent.value,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Support Agent",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }

                // Status Indicator
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF32CD32).copy(alpha = 0.2f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Online",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF32CD32),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Actions
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            val quickActions = listOf("Report Bug", "Feature Request", "Account Issue", "Technical Help")
            quickActions.forEach { action ->
                Button(
                    onClick = {
                        currentMessage.value = action
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 2.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF32CD32).copy(alpha = 0.2f),
                        contentColor = Color(0xFF32CD32)
                    )
                ) {
                    Text(action, fontSize = 10.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Chat Interface
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.6f))
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Messages
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    reverseLayout = false
                ) {
                    items(chatMessages) { message ->
                        SupportMessageBubble(message = message)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Typing indicator
                    if (isTyping.value) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.Black.copy(alpha = 0.6f)
                                    ),
                                    shape = RoundedCornerShape(
                                        topStart = 4.dp,
                                        topEnd = 16.dp,
                                        bottomStart = 16.dp,
                                        bottomEnd = 16.dp
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${supportAgent.value} is typing",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color(0xFF32CD32)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        // Simple typing animation
                                        Row {
                                            repeat(3) {
                                                Card(
                                                    modifier = Modifier
                                                        .size(4.dp)
                                                        .padding(horizontal = 1.dp),
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = Color(0xFF32CD32).copy(alpha = 0.5f)
                                                    ),
                                                    shape = CircleShape
                                                ) {}
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Message Input
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = currentMessage.value,
                        onValueChange = { currentMessage.value = it },
                        placeholder = { Text("Type your message...") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF32CD32),
                            unfocusedBorderColor = Color(0xFF32CD32).copy(alpha = 0.5f)
                        ),
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (currentMessage.value.isNotBlank()) {
                                chatMessages.add(
                                    SupportMessage(
                                        currentMessage.value,
                                        "You",
                                        true,
                                        "Now"
                                    )
                                )
                                currentMessage.value = ""

                                // Simulate agent response
                                isTyping.value = true
                                coroutineScope.launch {
                                    delay((1000..3000).random().toLong())
                                    isTyping.value = false
                                    chatMessages.add(
                                        SupportMessage(
                                            "Thank you for your message. I'm here to help! Let me look into this for you.",
                                            supportAgent.value,
                                            false,
                                            "Now"
                                        )
                                    )
                                }
                            }
                        },
                        modifier = Modifier.size(48.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color(0xFF32CD32)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = Color.Black
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Support Options
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { /* Start voice call */ },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF32CD32)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Voice Call",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Voice")
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedButton(
                onClick = { /* Start video call */ },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF32CD32)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Videocam,
                    contentDescription = "Video Call",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Video")
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedButton(
                onClick = { /* End chat */ },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFDC143C)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "End Chat",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("End")
            }
        }
    }
}

/**
 * Support message bubble component
 */
@Composable
private fun SupportMessageBubble(message: SupportMessage) {
    val isUser = message.isUser

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isUser)
                    Color(0xFF32CD32)
                else
                    Color.Black.copy(alpha = 0.6f)
            ),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                if (!isUser) {
                    Text(
                        text = message.sender,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF32CD32),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = message.timestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
        }
    }
}

/**
 * Data class for support messages
 */
data class SupportMessage(
    val content: String,
    val sender: String,
    val isUser: Boolean,
    val timestamp: String
)
