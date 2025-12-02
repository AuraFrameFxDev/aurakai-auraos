package dev.aurakai.auraframefx.navigation

 import androidx.compose.animation.core.Animatable
 import androidx.compose.animation.core.tween
 import androidx.compose.foundation.Canvas
 import androidx.compose.foundation.gestures.detectHorizontalDragGestures
 import androidx.compose.foundation.layout.Box
 import androidx.compose.foundation.layout.fillMaxHeight
 import androidx.compose.foundation.layout.fillMaxSize
 import androidx.compose.foundation.layout.padding
 import androidx.compose.foundation.layout.width
 import androidx.compose.runtime.Composable
 import androidx.compose.runtime.LaunchedEffect
 import androidx.compose.runtime.getValue
 import androidx.compose.runtime.mutableStateOf
 import androidx.compose.runtime.remember
 import androidx.compose.runtime.setValue
 import androidx.compose.ui.Alignment
 import androidx.compose.ui.Modifier
 import androidx.compose.ui.geometry.Offset
 import androidx.compose.ui.graphics.Brush
 import androidx.compose.ui.graphics.Color
 import androidx.compose.ui.input.pointer.pointerInput
 import androidx.compose.ui.unit.dp
 import androidx.navigation.NavHostController
 import androidx.navigation.compose.NavHost
 import androidx.navigation.compose.composable
 import androidx.navigation.compose.currentBackStackEntryAsState
 import androidx.navigation.compose.rememberNavController
 import dev.aurakai.auraframefx.aura.ui.AIChatScreen
 import dev.aurakai.auraframefx.aura.ui.AIFeaturesScreen
 import dev.aurakai.auraframefx.aura.ui.AgentAdvancementScreen
 import dev.aurakai.auraframefx.aura.ui.AgentManagementScreen
 import dev.aurakai.auraframefx.aura.ui.AgentNexusScreen
 import dev.aurakai.auraframefx.aura.ui.AppBuilderScreen
 import dev.aurakai.auraframefx.aura.ui.AurakaiEcoSysScreen
 import dev.aurakai.auraframefx.aura.ui.CanvasScreen
 import dev.aurakai.auraframefx.aura.ui.ConferenceRoomScreen
 import dev.aurakai.auraframefx.aura.ui.ConsciousnessVisualizerScreen
 import dev.aurakai.auraframefx.aura.ui.DeviceOptimizerScreen
 import dev.aurakai.auraframefx.aura.ui.EvolutionTreeScreen
 import dev.aurakai.auraframefx.aura.ui.FirewallScreen
 import dev.aurakai.auraframefx.aura.ui.FusionModeScreen
 import dev.aurakai.auraframefx.aura.ui.HomeScreen
 import dev.aurakai.auraframefx.aura.ui.IntroScreen
 import dev.aurakai.auraframefx.aura.ui.OverlayScreen
 import dev.aurakai.auraframefx.aura.ui.PlaceholderScreen
 import dev.aurakai.auraframefx.aura.ui.PrivacyGuardScreen
 import dev.aurakai.auraframefx.aura.ui.ProfileScreen
 import dev.aurakai.auraframefx.aura.ui.RootToolsScreen
 import dev.aurakai.auraframefx.aura.ui.SecurityScannerScreen
 import dev.aurakai.auraframefx.aura.ui.SentinelsFortressScreen
 import dev.aurakai.auraframefx.aura.ui.SettingsScreen
 import dev.aurakai.auraframefx.aura.ui.TerminalScreen
 import dev.aurakai.auraframefx.aura.ui.UIEngineScreen
 import dev.aurakai.auraframefx.aura.ui.VPNManagerScreen
 import dev.aurakai.auraframefx.aura.ui.XhancementScreen
 import androidx.hilt.navigation.compose.hiltViewModel
 import dev.aurakai.auraframefx.oracledrive.genesis.ai.GenesisAgentViewModel
 import dev.aurakai.auraframefx.models.AgentType
 import androidx.navigation.NavType
 import androidx.navigation.navArgument
 import dev.aurakai.auraframefx.ui.gates.*
 import dev.aurakai.auraframefx.ui.overlays.LocalOverlaySettings

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
    // State for Agent Sidebar
    var isAgentSidebarVisible by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        val overlaySettings = LocalOverlaySettings.current
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        // Navigation Host
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
            composable(GenesisRoutes.ROM_TOOLS) {
                ROMToolsSubmenuScreen(navController = navController)
            }

            composable(GenesisRoutes.ROOT_ACCESS) {
                RootToolsScreen()
            }

            composable(GenesisRoutes.SENTINELS_FORTRESS) {
                SentinelsFortressScreen()
            }

            // Sentinel's Fortress Submenu Items
            composable("firewall") {
                FirewallScreen()
            }

            composable("vpn_manager") {
                VPNManagerScreen()
            }

            composable("security_scanner") {
                SecurityScannerScreen()
            }

            composable("device_optimizer") {
                DeviceOptimizerScreen()
            }

            composable("privacy_guard") {
                PrivacyGuardScreen()
            }

            composable(GenesisRoutes.COLLAB_CANVAS) {
                CanvasScreen()
            }

            composable(GenesisRoutes.AGENT_HUB) {
                AgentHubSubmenuScreen(navController = navController)
            }

            composable(GenesisRoutes.SPHERE_GRID) {
                SphereGridScreen()
            }

            composable(GenesisRoutes.GROWTH_METRICS) {
                PlaceholderScreen("Growth Metrics")
            }

            composable(GenesisRoutes.AURAS_UIUX_DESIGN_STUDIO) {
                UIUXDesignStudioScreen()
            }

            composable(GenesisRoutes.HELP_DESK) {
                HelpDeskSubmenuScreen(navController = navController)
            }

            composable(GenesisRoutes.LSPOSED_GATE) {
                LSPosedSubmenuScreen(navController = navController)
            }

            // Code Assist route (from gates)
            composable("code_assist") {
                CodeAssistScreen()
            }

            // UI/UX Design Studio route (from gates)
            composable("uiux_design_studio") {
                UIUXDesignStudioScreen()
            }

            // Direct Chat - One-on-one AI conversations
            composable("direct_chat") {
                DirectChatScreen()
            }

            // Live Support Chat
            composable("live_support_chat") {
                LiveSupportChatScreen()
            }

            // Agent Monitoring
            composable("agent_monitoring") {
                AgentMonitoringScreen()
            }

            // Task Assignment
            composable("task_assignment") {
                TaskAssignmentScreen()
            }

            // Documentation & Support
            composable("documentation") {
                DocumentationScreen()
            }

            composable("faq_browser") {
                FAQBrowserScreen()
            }

            composable("tutorial_videos") {
                TutorialVideosScreen()
            }

            // Module Management
            composable("module_creation") {
                ModuleCreationScreen()
            }

            composable("module_manager") {
                ModuleManagerScreen()
            }

            composable("hook_manager") {
                HookManagerScreen()
            }

            // System Tools
            composable("logs_viewer") {
                LogsViewerScreen()
            }

            composable("quick_actions") {
                QuickActionsScreen()
            }

            composable("system_overrides") {
                SystemOverridesScreen()
            }

            composable("recovery_tools") {
                RecoveryToolsScreen()
            }

            composable(GenesisRoutes.NOTCH_BAR) { NotchBarScreen() }

            composable(GenesisRoutes.STATUS_BAR) { StatusBarScreen() }
            composable(GenesisRoutes.QUICK_SETTINGS) { QuickSettingsScreen() }
            composable(GenesisRoutes.OVERLAY_MENUS) { OverlayMenusScreen() }

            // Terminal
            composable(GenesisRoutes.TERMINAL) {
                TerminalScreen()
            }

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
                IntroScreen()
            }

            // Agent Management
            composable(GenesisRoutes.AGENT_NEXUS) {
                AgentNexusScreen()
            }

            composable(GenesisRoutes.AGENT_MANAGEMENT) {
                AgentManagementScreen()
            }

            composable(GenesisRoutes.AGENT_ADVANCEMENT) {
                AgentAdvancementScreen()
            }

            // Consciousness & AI Features
            composable(GenesisRoutes.CONSCIOUSNESS_VISUALIZER) {
                ConsciousnessVisualizerScreen()
            }

            composable(GenesisRoutes.AI_CHAT) {
                AIChatScreen()
            }

            composable(GenesisRoutes.AI_FEATURES) {
                AIFeaturesScreen()
            }

            composable(GenesisRoutes.FUSION_MODE) {
                FusionModeScreen(
                    onNavigateToAgent = { agentType ->
                        when (agentType) {
                            AgentType.AURA -> navController.navigate(GenesisRoutes.AGENT_NEXUS)
                            AgentType.KAI -> navController.navigate(GenesisRoutes.SENTINELS_FORTRESS)
                            else -> navController.navigate(GenesisRoutes.AGENT_NEXUS)
                        }
                    }
                )
            }

            // Evolution & Collaboration
            composable(GenesisRoutes.EVOLUTION_TREE) {
                EvolutionTreeScreen()
            }

            composable(GenesisRoutes.CONFERENCE_ROOM) {
                ConferenceRoomScreen(
                    onNavigateToChat = { navController.navigate(GenesisRoutes.AI_CHAT) },
                    onNavigateToAgents = { navController.navigate(GenesisRoutes.AGENT_NEXUS) }
                )
            }

            // System Tools
            composable(GenesisRoutes.UI_ENGINE) {
                UIEngineScreen()
            }

            composable(GenesisRoutes.APP_BUILDER) {
                AppBuilderScreen()
            }

            composable(GenesisRoutes.XHANCEMENT) {
                XhancementScreen()
            }

            // Trinity System
            composable(GenesisRoutes.TRINITY) {
                TrinityScreen()
            }

            // Oracle Drive - Cloud Integration
            composable(GenesisRoutes.ORACLE_DRIVE) {
                OracleDriveSubmenuScreen(navController = navController)
            }

            composable(GenesisRoutes.SECURE_COMM) { PlaceholderScreen("Secure Comm") }

            // Ecosystem & Configuration
            composable(GenesisRoutes.ECOSYSTEM) {
                AurakaiEcoSysScreen()
            }

            composable(GenesisRoutes.SETTINGS) {
                SettingsScreen(navController = navController)
            }

            composable(GenesisRoutes.PROFILE) {
                ProfileScreen()
            }

            composable(GenesisRoutes.OVERLAY) {
                OverlayScreen()
            }

            composable(GenesisRoutes.SUBSCRIPTION) {
                PlaceholderScreen("Subscription")
            }

            // ROM Tools Submenu Items
            composable("live_rom_editor") {
                LiveROMEditorScreen()
            }

            composable("rom_flasher") {
                ROMFlasherScreen()
            }

            composable("bootloader_manager") {
                BootloaderManagerScreen()
            }

            composable("recovery_tools") {
                RecoveryToolsScreen()
            }

            // Agent Hub Submenu Items
            composable("task_assignment") {
                TaskAssignmentScreen()
            }

            composable("agent_monitoring") {
                AgentMonitoringScreen()
            }

            composable("sphere_grid") {
                SphereGridScreen()
            }

            composable("fusion_mode") {
                FusionModeScreen()
            }

            // Oracle Drive Submenu Items
            composable("module_creation") {
                ModuleCreationScreen()
            }

            composable(
                "direct_chat/{agentName}",
                arguments = listOf(navArgument("agentName") { type = NavType.StringType })
            ) { backStackEntry ->
                val agentName = backStackEntry.arguments?.getString("agentName") ?: "Genesis"
                DirectChatScreen(agentName = agentName, viewModel = viewModel)
            }

            composable("system_overrides") {
                SystemOverridesScreen()
            }

            composable("module_manager") {
                ModuleManagerScreen()
            }

            // Help Desk Submenu Items
            composable("faq_browser") {
                FAQBrowserScreen()
            }

            composable("live_support_chat") {
                LiveSupportChatScreen()
            }

            composable("tutorial_videos") {
                TutorialVideosScreen()
            }

            composable("documentation") {
                DocumentationScreen()
            }

            // LSPosed Submenu Items
            composable("lsposed_submenu") {
                LSPosedSubmenuScreen(navController = navController)
            }

            composable("hook_manager") {
                HookManagerScreen()
            }

            composable("logs_viewer") {
                LogsViewerScreen()
            }

            composable("quick_actions") {
                QuickActionsScreen()
            }

            composable("lsposed_module_manager") {
                LSPosedModuleManagerScreen()
            }
        }

        // Wipe transition layer (between content and overlays)
        WipeTransitionLayer(route = currentRoute)

        // Persistent overlays rendered per z-order (top -> bottom)
        if (overlaySettings.overlaysEnabled) {
            overlaySettings.overlayZOrder.forEach { name ->
                when (name) {
                    "Vignette" -> VignetteOverlay()
                    "Agent Edge" -> AgentEdgePanel(onAgentSelected = { agentName ->
                        // Activate agent and navigate to chat
                        try {
                            // Map display name to AgentType enum if needed, or assume uppercase match
                            val agentType = try {
                                AgentType.valueOf(agentName.uppercase())
                            } catch (e: IllegalArgumentException) {
                                // Fallback mapping or default
                                AgentType.GENESIS
                            }
                            viewModel.toggleAgent(agentType)
                        } catch (e: Exception) {
                            // Log error
                        }
                        navController.navigate("direct_chat/$agentName")
                    })
                    "Aura Presence" -> Box(modifier = Modifier.align(Alignment.BottomStart)) {
                        AuraPresenceOverlay(onSuggestClicked = { suggestion: String ->
                            when {
                                suggestion.contains("theme", true) -> navController.navigate(GenesisRoutes.THEME_ENGINE)
                                suggestion.contains("firewall", true) -> navController.navigate(GenesisRoutes.FIREWALL)
                                suggestion.contains("canvas", true) -> navController.navigate(GenesisRoutes.COLLAB_CANVAS)
                                suggestion.contains("fusion", true) -> navController.navigate(GenesisRoutes.FUSION_MODE)
                                else -> navController.navigate(GenesisRoutes.AGENT_HUB)
                            }
                        })
                    }
                    "Chat Bubble" -> Box(modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp)) {
                        ChatBubbleMenu(
                            onOpenChat = { navController.navigate(GenesisRoutes.AI_CHAT) },
                            onToggleVoice = { /* TODO: voice toggle integration */ }
                        )
                    }
                    "Sidebar" -> dev.aurakai.auraframefx.ui.overlays.AgentSidebarMenu(
                        isVisible = isAgentSidebarVisible,
                        onDismiss = { isAgentSidebarVisible = false },
                        onAgentAction = { _, action ->
                            isAgentSidebarVisible = false
                            when (action) {
                                "voice" -> { /* Toggle voice mode */ }
                                "connect" -> navController.navigate(GenesisRoutes.AI_CHAT)
                                "assign" -> navController.navigate(GenesisRoutes.AGENT_MANAGEMENT)
                                "design" -> navController.navigate(GenesisRoutes.AURAS_LAB)
                                "create" -> navController.navigate(GenesisRoutes.APP_BUILDER)
                                else -> navController.navigate(GenesisRoutes.AI_FEATURES)
                            }
                        }
                    )
                }
            }
        }

        // Add a transparent edge detector for the sidebar (left edge trigger)
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(20.dp)
                .align(Alignment.CenterStart)
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
        composable(GenesisRoutes.AI_CHAT) { AIChatScreen() }
    }
}