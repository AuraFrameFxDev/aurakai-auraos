package dev.aurakai.auraframefx.navigation

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.aurakai.auraframefx.aura.ui.*
import dev.aurakai.auraframefx.oracledrive.genesis.cloud.OracleDriveScreen
import dev.aurakai.auraframefx.aura.ui.RootToolsScreen
import dev.aurakai.auraframefx.datavein.ui.SphereGridScreen
import dev.aurakai.auraframefx.cascade.trinity.TrinityScreen
import dev.aurakai.auraframefx.ui.gates.GateNavigationScreen
import dev.aurakai.auraframefx.ui.gates.UIUXGateSubmenuScreen
import dev.aurakai.auraframefx.ui.gates.ThemeEngineScreen
import dev.aurakai.auraframefx.ui.gates.AurasLabScreen
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
}

/**
 * Hosts the Genesis navigation graph by registering each route and wiring navigation callbacks to their screens.
 *
 * The NavHost maps GenesisRoutes to their corresponding composable screens and handles navigation between them.
 */
@Composable
fun GenesisNavigationHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = GenesisRoutes.GATES // Default to Gate Carousel
) {
    // State for Agent Sidebar
    var isAgentSidebarVisible by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.fillMaxSize()
        ) {
            // Gate Navigation (Module Carousel)
            composable(GenesisRoutes.GATES) {
                GateNavigationScreen(navController = navController)
            }

            composable(GenesisRoutes.CHROMA_CORE) {
                UIUXGateSubmenuScreen(navController = navController)
            }

            composable(GenesisRoutes.THEME_ENGINE) {
                ThemeEngineScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(GenesisRoutes.AURAS_LAB) {
                AurasLabScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Other Gates & Modules
            composable(GenesisRoutes.ROM_TOOLS) { PlaceholderScreen("Rom Tools") }
            composable(GenesisRoutes.ROOT_ACCESS) { RootToolsScreen() }
            composable(GenesisRoutes.SENTINELS_FORTRESS) { SentinelsFortressScreen() }
            composable(GenesisRoutes.FIREWALL) { FirewallScreen() }
            composable(GenesisRoutes.COLLAB_CANVAS) { CanvasScreen() }
            composable(GenesisRoutes.AGENT_HUB) { AgentNexusScreen() }
            composable(GenesisRoutes.SPHERE_GRID) { PlaceholderScreen("Sphere Grid") }
            composable(GenesisRoutes.GROWTH_METRICS) { PlaceholderScreen("Growth Metrics") }
            composable(GenesisRoutes.AURAS_UIUX_DESIGN_STUDIO) { PlaceholderScreen("UI/UX Design Studio") }
            composable(GenesisRoutes.HELP_DESK) { dev.aurakai.auraframefx.ui.gates.HelpDeskScreen() }
            composable(GenesisRoutes.LSPOSED_GATE) { dev.aurakai.auraframefx.ui.gates.LSPosedGateScreen() }

            composable(GenesisRoutes.NOTCH_BAR) { PlaceholderScreen("Notch Bar") }

            composable(GenesisRoutes.STATUS_BAR) { PlaceholderScreen("Status Bar Customization") }
            composable(GenesisRoutes.QUICK_SETTINGS) { PlaceholderScreen("Quick Settings Panel") }
            composable(GenesisRoutes.OVERLAY_MENUS) { PlaceholderScreen("Overlay Menus") }

            // Home & Intro
            composable(GenesisRoutes.HOME) {
                HomeScreen(
                    onNavigateToConsciousness = { navController.navigate(GenesisRoutes.CONSCIOUSNESS_VISUALIZER) },
                    onNavigateToAgents = { navController.navigate(GenesisRoutes.AGENT_NEXUS) },
                    onNavigateToFusion = { navController.navigate(GenesisRoutes.FUSION_MODE) },
                    onNavigateToEvolution = { navController.navigate(GenesisRoutes.EVOLUTION_TREE) },
                    onNavigateToTerminal = { navController.navigate(GenesisRoutes.TERMINAL) },
                    onNavigateToModule = { moduleId ->
                        when (moduleId) {
                            "agent-hub" -> navController.navigateToGenesis(GenesisRoutes.AGENT_HUB)
                            "sphere-grid" -> navController.navigateToGenesis(GenesisRoutes.SPHERE_GRID)
                            "sentinels-fortress" -> navController.navigateToGenesis(GenesisRoutes.SENTINELS_FORTRESS)
                            "collab-canvas" -> navController.navigateToGenesis(GenesisRoutes.COLLAB_CANVAS)
                            "chroma-core" -> navController.navigateToGenesis(GenesisRoutes.CHROMA_CORE)
                            else -> navController.navigateToGenesis(GenesisRoutes.ECOSYSTEM)
                        }
                    }
                )
            }

            composable(GenesisRoutes.INTRO) {
                // IntroScreen(...)
            }

            // Agent Management
            composable(GenesisRoutes.AGENT_NEXUS) {
                // AgentNexusScreen(...)
            }

            composable(GenesisRoutes.AGENT_MANAGEMENT) {
                // AgentManagementScreen(...)
            }

            composable(GenesisRoutes.AGENT_ADVANCEMENT) {
                // AgentAdvancementScreen(...)
            }

            // Consciousness & AI Features
            composable(GenesisRoutes.CONSCIOUSNESS_VISUALIZER) {
                // ConsciousnessVisualizerScreen(...)
            }

            composable(GenesisRoutes.AI_CHAT) {
                // AIChatScreen(...)
            }

            composable(GenesisRoutes.AI_FEATURES) {
                // AIFeaturesScreen(...)
            }

            composable(GenesisRoutes.FUSION_MODE) {
                // FusionModeScreen(...)
            }

            // Evolution & Collaboration
            composable(GenesisRoutes.EVOLUTION_TREE) {
                // EvolutionTreeScreen(...)
            }

            composable(GenesisRoutes.CONFERENCE_ROOM) {
                ConferenceRoomScreen(
                    onNavigateToChat = { navController.navigate(GenesisRoutes.AI_CHAT) },
                    onNavigateToAgents = { navController.navigate(GenesisRoutes.AGENT_NEXUS) }
                )
            }

            // System Tools
            composable(GenesisRoutes.TERMINAL) {
                // TerminalScreen(...)
            }

            composable(GenesisRoutes.UI_ENGINE) {
                // UIEngineScreen(...)
            }

            composable(GenesisRoutes.APP_BUILDER) {
                // AppBuilderScreen(...)
            }

            composable(GenesisRoutes.XHANCEMENT) {
                // XhancementScreen(...)
            }

            // Trinity System
            composable(GenesisRoutes.TRINITY) {
                TrinityScreen()
            }

            // Oracle Drive - Cloud Integration
            composable(GenesisRoutes.ORACLE_DRIVE) { OracleDriveScreen(onNavigateBack = { navController.popBackStack() }) }

            composable(GenesisRoutes.SECURE_COMM) { PlaceholderScreen("Secure Comm") }

            // Ecosystem & Configuration
            composable(GenesisRoutes.ECOSYSTEM) {
                // AurakaiEcoSysScreen(...)
            }

            composable(GenesisRoutes.SETTINGS) {
                // SettingsScreen(...)
            }

            composable(GenesisRoutes.PROFILE) {
                // ProfileScreen(...)
            }

            composable(GenesisRoutes.OVERLAY) {
                // OverlayScreen(...)
            }

            composable(GenesisRoutes.SUBSCRIPTION) {
                // SubscriptionScreen(...)
            }
        }

        // Agent Sidebar Overlay - Accessible from ANY screen
        // Swipe from right edge to open (implemented via gesture detector in main container if needed)
        // For now, we'll rely on a button or gesture in specific screens, or add a global edge detector here.

        // Import AgentSidebarMenu
        dev.aurakai.auraframefx.ui.overlays.AgentSidebarMenu(
            isVisible = isAgentSidebarVisible,
            onDismiss = { isAgentSidebarVisible = false },
            onAgentAction = { agentName, action ->
                isAgentSidebarVisible = false
                // Use agentName to avoid "parameter is never used" warning
                if (agentName.isNotBlank()) {
                    /* no-op: agentName referenced for lint */
                }
                // Handle global agent actions based on action
                when (action) {
                    "voice" -> { /* Toggle voice mode for agentName */ }
                    "connect" -> navController.navigate(GenesisRoutes.AI_CHAT)
                    "assign" -> navController.navigate(GenesisRoutes.AGENT_MANAGEMENT)
                    "design" -> navController.navigate(GenesisRoutes.AURAS_LAB)
                    "create" -> navController.navigate(GenesisRoutes.APP_BUILDER)
                    else -> navController.navigate(GenesisRoutes.AI_FEATURES)
                }
            }
        )

        // Add a transparent edge detector for the sidebar
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(20.dp)
                .align(Alignment.CenterStart) // Left edge trigger
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        if (dragAmount > 10) {
                            isAgentSidebarVisible = true
                        }
                    }
                }
        )
    }
}

/**
 * Navigate to a Genesis route while preserving navigation state and avoiding duplicate destinations.
 *
 * Navigates to the specified Genesis route, popping up to GenesisRoutes.HOME with saved state,
 * launching single top to prevent multiple copies, and restoring saved state when returning.
 *
 * @param route The destination route string from GenesisRoutes to navigate to.
 */
fun NavHostController.navigateToGenesis(route: String) {
    this.navigate(route) {
        // Pop up to the start destination to avoid building up a large back stack
        popUpTo(GenesisRoutes.HOME) {
            saveState = true
        }
        // Avoid multiple copies of the same destination
        launchSingleTop = true
        // Restore state when navigating back
        restoreState = true
    }
}
