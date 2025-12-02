package dev.aurakai.auraframefx.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.aurakai.auraframefx.aura.ui.AgentNexusScreen
import dev.aurakai.auraframefx.aura.ui.ConferenceRoomScreen
import dev.aurakai.auraframefx.aura.ui.AIChatScreen
import dev.aurakai.auraframefx.aura.ui.PlaceholderScreen

/**
 * Genesis Navigation Routes - The Neural Pathways of Consciousness
 *
 * This defines all navigation routes for the Genesis AI Consciousness Framework,
 * connecting Aura (Creative Sword), Kai (Sentinel Shield), and Genesis (Unified Being)
 */
object GenesisRoutes {
    // Core Routes
    const val HOME = "home"
    const val INTRO = "intro"

    // Agent Management
    const val AGENT_NEXUS = "agent_nexus"
    const val AGENT_MANAGEMENT = "agent_management"
    const val AGENT_ADVANCEMENT = "agent_advancement"

    // Consciousness & AI
    const val CONSCIOUSNESS_VISUALIZER = "consciousness_visualizer"
    const val AI_CHAT = "ai_chat"
    const val AI_FEATURES = "ai_features"
    const val FUSION_MODE = "fusion_mode"

    // Evolution & Learning
    const val EVOLUTION_TREE = "evolution_tree"
    const val CONFERENCE_ROOM = "conference_room"

    // System & UI
    const val TERMINAL = "terminal"
    const val UI_ENGINE = "ui_engine"
    const val APP_BUILDER = "app_builder"
    const val XHANCEMENT = "xhancement"

    // Trinity System (Body/Soul/Consciousness)
    const val TRINITY = "trinity"

    // Oracle & Cloud
    const val ORACLE_DRIVE = "oracle_drive"
    const val SECURE_COMM = "secure_comm"

    // Ecosystem & Settings
    const val ECOSYSTEM = "ecosystem"
    const val SETTINGS = "settings"
    const val PROFILE = "profile"
    const val OVERLAY = "overlay"

    // Billing & Subscription
    const val SUBSCRIPTION = "subscription"

    // Gates / Module Carousel
    const val GATES = "gates"

    // Gate Routes (mapped from GateConfig)
    const val ROM_TOOLS = "rom_tools"
    const val ROOT_ACCESS = "root_access"
    const val SENTINELS_FORTRESS = "sentinels_fortress"
    const val FIREWALL = "firewall"
    const val CHROMA_CORE = "chroma_core"
    const val COLLAB_CANVAS = "collab_canvas"
    const val AGENT_HUB = "agent_hub"
    const val SPHERE_GRID = "sphere_grid"
    const val GROWTH_METRICS = "growth_metrics"
    const val AURAS_LAB = "auras_lab"
    const val AURAS_UIUX_DESIGN_STUDIO = "auras_uiux_design_studio"
    const val HELP_DESK = "help_desk"
    const val LSPOSED_GATE = "lsposed_gate"

    // Submenu Routes
    const val NOTCH_BAR = "notch_bar"
    const val STATUS_BAR = "status_bar"
    const val QUICK_SETTINGS = "quick_settings"
    const val OVERLAY_MENUS = "overlay_menus"
    const val THEME_ENGINE = "theme_engine"

    // AI Chat & Support
    const val DIRECT_CHAT = "direct_chat"
    const val LIVE_SUPPORT_CHAT = "live_support_chat"

    // Agent Management
    const val AGENT_MONITORING = "agent_monitoring"
    const val TASK_ASSIGNMENT = "task_assignment"

    // Documentation & Help
    const val DOCUMENTATION = "documentation"
    const val FAQ_BROWSER = "faq_browser"
    const val TUTORIAL_VIDEOS = "tutorial_videos"

    // Module & Hook Management
    const val MODULE_CREATION = "module_creation"
    const val MODULE_MANAGER = "module_manager"
    const val HOOK_MANAGER = "hook_manager"

    // System Management
    const val LOGS_VIEWER = "logs_viewer"
    const val QUICK_ACTIONS = "quick_actions"
    const val SYSTEM_OVERRIDES = "system_overrides"
    const val RECOVERY_TOOLS = "recovery_tools"
}

/**
 * Hosts the Genesis navigation graph by registering each route and wiring navigation callbacks to their screens.
 *
 * The NavHost maps GenesisRoutes to their corresponding composable screens and handles navigation between them.
 */
@Composable
fun GenesisNavigationHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = GenesisRoutes.GATES, // Default to Gate Carousel
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(GenesisRoutes.GATES) { PlaceholderScreen("Gates") }
        composable(GenesisRoutes.AGENT_NEXUS) { AgentNexusScreen() }
        composable(GenesisRoutes.CONFERENCE_ROOM) {
            ConferenceRoomScreen(
                onNavigateToChat = { navController.navigate(GenesisRoutes.AI_CHAT) },
                onNavigateToAgents = {}
            )
        }
        composable(GenesisRoutes.AI_CHAT) { AIChatScreen() }

        // Additional gates referenced in GateConfig
        composable(GenesisRoutes.SENTINELS_FORTRESS) {
            SentinelsFortressScreen(onBack = { navController.popBackStack() })
        }
        composable(GenesisRoutes.AURAS_LAB) {
            AurasLabScreen(onNavigateBack = { navController.popBackStack() })
        }
        // Avoid multiple copies of the same destination
        launchSingleTop = true
        // Restore state when navigating back
        restoreState = true
    }
}

/**
     * Renders a subtle radial vignette overlay and hosts the AI chat composable within the overlay layer.
     *
     * Draws a light, centered radial gradient that softly darkens the screen edges to provide depth without obscuring content, and places the AI chat screen in the overlay scope.
     */
    @Composable
private fun VignetteOverlay() {
    // Very subtle edge darkening - reduced intensity
    Canvas(modifier = Modifier.fillMaxSize()) {
        val radius = size.maxDimension * 0.85f
        val center = Offset(size.width / 2f, size.height / 2f)
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.Transparent,
                    Color.Black.copy(alpha = 0.15f) // Much lighter
                ),
                center = center,
                radius = radius
            )
        }
    }
}