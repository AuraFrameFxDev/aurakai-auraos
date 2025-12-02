package dev.aurakai.auraframefx.customization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import dev.aurakai.auraframefx.ui.theme.GlassmorphicTheme

/**
 * 🎨 Theme Manager - Global Theme State
 * 
 * Manages app-wide theme customization with live updates
 */

data class AppTheme(
    // Primary colors
    val primary: Color = GlassmorphicTheme.Primary,
    val primaryVariant: Color = GlassmorphicTheme.PrimaryVariant,
    
    // Secondary colors
    val secondary: Color = GlassmorphicTheme.Secondary,
    val secondaryVariant: Color = GlassmorphicTheme.SecondaryVariant,
    
    // Accent colors
    val accent: Color = GlassmorphicTheme.Accent,
    val accentGold: Color = GlassmorphicTheme.AccentGold,
    
    // Background colors
    val background: Color = GlassmorphicTheme.Background,
    val surface: Color = GlassmorphicTheme.Surface,
    
    // Glass effects
    val glassOpacity: Float = 0.1f, // 10% default
    val glassBorderOpacity: Float = 0.2f, // 20% default
    
    // Typography
    val fontSize: FontSize = FontSize.MEDIUM,
    val fontWeight: FontWeightStyle = FontWeightStyle.NORMAL,
    
    // Effects
    val blurEnabled: Boolean = true,
    val glowEnabled: Boolean = true,
    val animationsEnabled: Boolean = true,
    
    // Theme preset
    val preset: ThemePreset = ThemePreset.GLASSMORPHIC
)

enum class ThemePreset {
    GLASSMORPHIC,      // Default - Professional glass
    CYBERPUNK,         // Neon & vibrant
    FINAL_FANTASY,     // Ethereal & mystical
    MINIMAL,           // Clean & simple
    DARK_MATTER,       // Deep blacks
    CUSTOM             // User-defined
}

enum class FontSize {
    SMALL,
    MEDIUM,
    LARGE,
    EXTRA_LARGE
}

enum class FontWeightStyle {
    LIGHT,
    NORMAL,
    MEDIUM,
    BOLD
}

/**
 * CompositionLocal for theme access
 */
val LocalAppTheme = compositionLocalOf { AppTheme() }

/**
 * Theme provider composable
 */
@Composable
fun AppThemeProvider(
    theme: AppTheme = AppTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalAppTheme provides theme) {
        content()
    }
}

/**
 * Get current theme
 */
@Composable
fun currentTheme(): AppTheme {
    return LocalAppTheme.current
}
