package dev.aurakai.auraframefx.customization

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Master ViewModel that aggregates >100 customization options via CustomizationPreferences.
 * Controls themes, glass effects, animations, UI elements toggles, and agent colors.
 */
@HiltViewModel
class CustomizationViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(CustomizationState())
    val state: StateFlow<CustomizationState> = _state.asStateFlow()

    data class CustomizationState(
        val themeName: String = "CyberGlow",
        val themeAccent: String = "NeonBlue",
        val themeDark: Boolean = true,
        val glassEnabled: Boolean = false,
        val glassBlurRadiusDp: Float = 0f,
        val glassSurfaceAlpha: Float = 0.12f,
        val animationsEnabled: Boolean = false,
        val animationSpeed: Int = 0,
        val showStatusBar: Boolean = true,
        val showNotchBar: Boolean = false,
        val showOverlayMenus: Boolean = false,
        val agentColors: Map<String, String> = emptyMap()
    )

    fun start(context: Context) {
        viewModelScope.launch {
            combine(
                CustomizationPreferences.themeNameFlow(context),
                CustomizationPreferences.themeAccentFlow(context),
                CustomizationPreferences.themeDarkFlow(context),
                CustomizationPreferences.glassEnabledFlow(context),
                CustomizationPreferences.glassBlurRadiusFlow(context),
                CustomizationPreferences.glassSurfaceAlphaFlow(context),
                CustomizationPreferences.animationsEnabledFlow(context),
                CustomizationPreferences.animationSpeedFlow(context),
                CustomizationPreferences.showStatusBarFlow(context),
                CustomizationPreferences.showNotchBarFlow(context),
                CustomizationPreferences.showOverlayMenusFlow(context)
            ) { name, accent, dark, glassEnabled, blur, alpha, animEnabled, animSpeed, status, notch, overlay ->
                _state.value = _state.value.copy(
                    themeName = name,
                    themeAccent = accent,
                    themeDark = dark,
                    glassEnabled = glassEnabled,
                    glassBlurRadiusDp = blur,
                    glassSurfaceAlpha = alpha,
                    animationsEnabled = animEnabled,
                    animationSpeed = animSpeed,
                    showStatusBar = status,
                    showNotchBar = notch,
                    showOverlayMenus = overlay
                )
            }
        }
    }

    fun setTheme(context: Context, name: String, accent: String, dark: Boolean) {
        viewModelScope.launch {
            CustomizationPreferences.setTheme(context, name, accent, dark)
        }
    }

    fun setGlass(context: Context, enabled: Boolean, blurRadiusDp: Float, surfaceAlpha: Float) {
        viewModelScope.launch {
            CustomizationPreferences.setGlass(context, enabled, blurRadiusDp, surfaceAlpha)
        }
    }

    fun setAnimations(context: Context, enabled: Boolean, speed: Int) {
        viewModelScope.launch {
            CustomizationPreferences.setAnimations(context, enabled, speed)
        }
    }

    fun setUiElements(context: Context, showStatusBar: Boolean, showNotchBar: Boolean, showOverlayMenus: Boolean) {
        viewModelScope.launch {
            CustomizationPreferences.setUiElements(context, showStatusBar, showNotchBar, showOverlayMenus)
        }
    }

    fun setAgentColor(context: Context, agentName: String, hexColor: String) {
        viewModelScope.launch {
            CustomizationPreferences.setAgentColor(context, agentName, hexColor)
            _state.value = _state.value.copy(
                agentColors = _state.value.agentColors + (agentName to hexColor)
            )
        }
    }
}

