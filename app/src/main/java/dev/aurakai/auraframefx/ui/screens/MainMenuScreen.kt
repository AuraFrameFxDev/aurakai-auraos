package dev.aurakai.auraframefx.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.sin

// Cyberpunk color palette matching the images
private val CyanGrid = Color(0xFF00F5FF)
private val MagentaGlow = Color(0xFFFF00FF)
private val DarkBg = Color(0xFF0A0A0A)
private val PurpleHolo = Color(0xFF9D00FF)

/**
 * Main Menu Screen - Holographic 3D Interface
 * 
 * Matches the aesthetic from the uploaded images:
 * - Cyan grid borders
 * - Holographic floating panels
 * - Central 3D hologram effect
 * - Retro-futuristic design
 */
@Composable
fun MainMenuScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToMissions: () -> Unit = {},
    onNavigateToInventory: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var selectedItem by remember { mutableStateOf("HOME") }
    
    // Animated hologram pulse
    val infiniteTransition = rememberInfiniteTransition(label = "hologram")
    val hologramPulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    // Grid scan line animation
    val scanLineOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scan"
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DarkBg,
                        Color(0xFF1A0B2E),
                        DarkBg
                    )
                )
            )
    ) {
        // Starfield background
        StarfieldBackground()
        
        // Central hologram area
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(300.dp)
                .scale(hologramPulse)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            PurpleHolo.copy(alpha = 0.3f),
                            MagentaGlow.copy(alpha = 0.1f),
                            Color.Transparent
                        )
                    )
                )
        ) {
            // Central hologram rings
            HologramRings()
        }
        
        // Main menu panel
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .width(400.dp)
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(CyanGrid, PurpleHolo, CyanGrid)
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .background(
                    color = DarkBg.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title
            Text(
                text = "MAIN MENU",
                style = MaterialTheme.typography.headlineMedium,
                color = CyanGrid,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Menu items
            MenuHoloButton(
                text = "HOME",
                isSelected = selectedItem == "HOME",
                onClick = {
                    selectedItem = "HOME"
                    onNavigateToHome()
                }
            )
            
            MenuHoloButton(
                text = "MISSIONS",
                isSelected = selectedItem == "MISSIONS",
                onClick = {
                    selectedItem = "MISSIONS"
                    onNavigateToMissions()
                }
            )
            
            MenuHoloButton(
                text = "INVENTORY",
                isSelected = selectedItem == "INVENTORY",
                onClick = {
                    selectedItem = "INVENTORY"
                    onNavigateToInventory()
                }
            )
            
            MenuHoloButton(
                text = "SETTINGS",
                isSelected = selectedItem == "SETTINGS",
                onClick = {
                    selectedItem = "SETTINGS"
                    onNavigateToSettings()
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            MenuHoloButton(
                text = "LOGOUT",
                isSelected = selectedItem == "LOGOUT",
                onClick = {
                    selectedItem = "LOGOUT"
                    onLogout()
                },
                isDestructive = true
            )
        }
        
        // Side panels (like in the images)
        SidePanel(modifier = Modifier.align(Alignment.CenterStart))
        SidePanel(modifier = Modifier.align(Alignment.CenterEnd))
        
        // Bottom hint text
        Text(
            text = "© Swipe left/right",
            color = CyanGrid.copy(alpha = 0.5f),
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}

@Composable
fun MenuHoloButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        label = "buttonScale"
    )
    
    val color = when {
        isDestructive -> Color(0xFFFF0055)
        isSelected -> MagentaGlow
        else -> CyanGrid
    }
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = color.copy(alpha = if (isSelected) 1f else 0.5f),
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = if (isSelected) color.copy(alpha = 0.2f) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 18.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            letterSpacing = 2.sp,
            textAlign = TextAlign.Center
        )
        
        // Glitch effect when selected
        if (isSelected) {
            Text(
                text = text,
                color = color.copy(alpha = 0.3f),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.offset(x = 2.dp, y = 2.dp)
            )
        }
    }
}

@Composable
fun HologramRings() {
    val infiniteTransition = rememberInfiniteTransition(label = "rings")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    // Draw concentric rotating rings (simplified - would need Canvas for full effect)
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        repeat(3) { index ->
            Box(
                modifier = Modifier
                    .size((100 + index * 50).dp)
                    .border(
                        width = 1.dp,
                        color = MagentaGlow.copy(alpha = 0.3f - index * 0.1f),
                        shape = RoundedCornerShape(50)
                    )
            )
        }
    }
}

@Composable
fun SidePanel(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(80.dp)
            .height(400.dp)
            .padding(16.dp)
            .border(
                width = 1.dp,
                color = CyanGrid.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = DarkBg.copy(alpha = 0.6f),
                shape = RoundedCornerShape(8.dp)
            )
            .alpha(0.5f)
    )
}

@Composable
fun StarfieldBackground() {
    // Simplified starfield - would use Canvas for actual stars
    Box(modifier = Modifier.fillMaxSize()) {
        repeat(50) { index ->
            val x = remember { (0..100).random() }
            val y = remember { (0..100).random() }
            val size = remember { (1..3).random() }
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = (x * 10).dp,
                        top = (y * 8).dp
                    )
            ) {
                Box(
                    modifier = Modifier
                        .size(size.dp)
                        .background(CyanGrid.copy(alpha = 0.6f))
                )
            }
        }
    }
}
