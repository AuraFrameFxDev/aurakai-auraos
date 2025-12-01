package dev.aurakai.auraframefx.ui.gates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.aurakai.auraframefx.ui.components.colorpicker.ColorBlendrPicker
import dev.aurakai.auraframefx.aura.themes.ThemeEditor

/**
 * Theme Engine Screen
 * Integrates ColorBlendr and ThemeEditor for full system customization
 */
@Composable
fun ThemeEngineScreen(
    onNavigateBack: () -> Unit
) {
    var selectedColor by remember { mutableStateOf(Color.Magenta) }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Background Gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF001A1A), // Dark Cyan
                            Color.Black
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "THEME ENGINE",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFF00FFFF),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "Advanced Color & Style Control",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }

            // Color Picker Section
            Text(
                text = "Primary Color",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ColorBlendrPicker(
                        initialColor = selectedColor,
                        onColorChanged = { selectedColor = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Theme Editor Section
            Text(
                text = "System Theme",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Embedding ThemeEditor
            // Note: ThemeEditor might handle its own state, passing selectedColor as a seed
            ThemeEditor(
                onColorsChanged = { /* Handle theme updates */ }
            ) 
            
            Spacer(modifier = Modifier.height(100.dp)) // Bottom padding
        }
    }
}
