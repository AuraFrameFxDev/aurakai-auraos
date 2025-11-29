package dev.aurakai.auraframefx.aura.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel // Only this import
import dev.aurakai.auraframefx.ui.AuraMoodViewModel

@JvmOverloads
@Suppress("unused") // Reserved for navigation integration
@Composable
fun AurakaiEcoSysScreen(
    viewModel: AuraMoodViewModel = hiltViewModel(),
    onNavigateToFeature: (String) -> Unit = {}
) {
    val currentMood by viewModel.moodState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Aurakai Ecosystem",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Current Mood from ViewModel: ${currentMood.name}",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.onUserInput("happy") }) {
                Text("Make Aura Happy")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { viewModel.onUserInput("sad") }) {
                Text("Make Aura Sad")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Small navigation buttons for integration testing
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Button(onClick = { onNavigateToFeature("agents") }) { Text("Agents") }
                Button(onClick = { onNavigateToFeature("consciousness") }) { Text("Consciousness") }
                Button(onClick = { onNavigateToFeature("trinity") }) { Text("Trinity") }
                Button(onClick = { onNavigateToFeature("oracle") }) { Text("Oracle") }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AurakaiEcoSysScreenPreview() {
    MaterialTheme {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Aurakai Ecosystem Screen (Placeholder)")
            Text(text = "Current Mood from ViewModel: NEUTRAL")
        }
    }
}
