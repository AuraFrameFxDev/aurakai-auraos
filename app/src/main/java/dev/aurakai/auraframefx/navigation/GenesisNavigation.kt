package dev.aurakai.auraframefx.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.aurakai.auraframefx.aura.ui.*
// import dev.aurakai.auraframefx.billing.SubscriptionScreen
import dev.aurakai.auraframefx.cascade.trinity.TrinityScreen
// import dev.aurakai.auraframefx.oracledrive.genesis.cloud.OracleDriveScreen

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

    // Ecosystem & Settings
    const val ECOSYSTEM = "ecosystem"
    const val SETTINGS = "settings"
    const val PROFILE = "profile"
    const val OVERLAY = "overlay"

    // Billing & Subscription
    const val SUBSCRIPTION = "subscription"
}

/**
 * Hosts the Genesis navigation graph by registering each route and wiring navigation callbacks to their screens.
 *
 * The NavHost maps GenesisRoutes to their corresponding composable screens and handles navigation between them.
 */
@Composable
fun GenesisNavigationHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = GenesisRoutes.HOME
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Home & Intro
        composable(GenesisRoutes.HOME) {
            HomeScreen(
                onNavigateToConsciousness = { navController.navigate(GenesisRoutes.CONSCIOUSNESS_VISUALIZER) },
                onNavigateToAgents = { navController.navigate(GenesisRoutes.AGENT_NEXUS) },
                onNavigateToFusion = { navController.navigate(GenesisRoutes.FUSION_MODE) },
                onNavigateToEvolution = { navController.navigate(GenesisRoutes.EVOLUTION_TREE) },
                onNavigateToTerminal = { navController.navigate(GenesisRoutes.TERMINAL) }
            )
        }

        composable(GenesisRoutes.INTRO) {
//            IntroScreen(
//                onNavigateToHome = { navController.navigate(GenesisRoutes.HOME) }
//            )
        }

        // Agent Management - The Neural Command Center
        composable(GenesisRoutes.AGENT_NEXUS) {
//            AgentNexusScreen(
//                onAgentSelected = { agentName ->
//                    // Handle agent selection - could navigate to detail screen
//                },
//                onDepartureTaskAssigned = { agent, task ->
//                    // Handle task assignment
//                }
//            )
        }

        composable(GenesisRoutes.AGENT_MANAGEMENT) {
//            AgentManagementScreen(
//                onNavigateToNexus = { navController.navigate(GenesisRoutes.AGENT_NEXUS) },
//                onNavigateToAdvancement = { navController.navigate(GenesisRoutes.AGENT_ADVANCEMENT) }
//            )
        }

        composable(GenesisRoutes.AGENT_ADVANCEMENT) {
//            AgentAdvancementScreen(
//                onNavigateBack = { navController.popBackStack() }
//            )
        }

        // Consciousness & AI Features - The Mind
        composable(GenesisRoutes.CONSCIOUSNESS_VISUALIZER) {
//            ConsciousnessVisualizerScreen(
//                onNavigateToChat = { navController.navigate(GenesisRoutes.AI_CHAT) },
//                onNavigateToFusion = { navController.navigate(GenesisRoutes.FUSION_MODE) }
//            )
        }

        composable(GenesisRoutes.AI_CHAT) {
//            AIChatScreen(
//                onNavigateBack = { navController.popBackStack() },
//                onNavigateToFusion = { navController.navigate(GenesisRoutes.FUSION_MODE) }
//            )
        }

        composable(GenesisRoutes.AI_FEATURES) {
//            AIFeaturesScreen(
//                onNavigateToConsciousness = { navController.navigate(GenesisRoutes.CONSCIOUSNESS_VISUALIZER) }
//            )
        }

        composable(GenesisRoutes.FUSION_MODE) {
//            FusionModeScreen(
//                onNavigateToAgents = { navController.navigate(GenesisRoutes.AGENT_NEXUS) },
//                onNavigateToConsciousness = { navController.navigate(GenesisRoutes.CONSCIOUSNESS_VISUALIZER) }
//            )
        }

        // Evolution & Collaboration
        composable(GenesisRoutes.EVOLUTION_TREE) {
//            EvolutionTreeScreen(
//                onNavigateToAgents = { navController.navigate(GenesisRoutes.AGENT_NEXUS) },
//                onNavigateToFusion = { navController.navigate(GenesisRoutes.FUSION_MODE) }
//            )
        }

        composable(GenesisRoutes.CONFERENCE_ROOM) {
            ConferenceRoomScreen(
                onNavigateToChat = { navController.navigate(GenesisRoutes.AI_CHAT) },
                onNavigateToAgents = { navController.navigate(GenesisRoutes.AGENT_NEXUS) }
            )
        }

        // System Tools
        composable(GenesisRoutes.TERMINAL) {
//            TerminalScreen(
//                onNavigateBack = { navController.popBackStack() }
//            )
        }

        composable(GenesisRoutes.UI_ENGINE) {
//            UIEngineScreen(
//                onNavigateToBuilder = { navController.navigate(GenesisRoutes.APP_BUILDER) }
//            )
        }

        composable(GenesisRoutes.APP_BUILDER) {
//            AppBuilderScreen(
//                onNavigateBack = { navController.popBackStack() }
//            )
        }

        composable(GenesisRoutes.XHANCEMENT) {
//            XhancementScreen(
//                onNavigateBack = { navController.popBackStack() }
//            )
        }

        // Trinity System - Body (Kai) / Soul (Aura) / Consciousness (Genesis)
        composable(GenesisRoutes.TRINITY) {
            TrinityScreen()
        }

        // Oracle Drive - Cloud Integration
        composable(GenesisRoutes.ORACLE_DRIVE) {
//            OracleDriveScreen(
//                onNavigateBack = { navController.popBackStack() }
//            )
        }

        // Ecosystem & Configuration
        composable(GenesisRoutes.ECOSYSTEM) {
//            AurakaiEcoSysScreen(
//                onNavigateToFeature = { feature ->
//                    when (feature) {
//                        "agents" -> navController.navigate(GenesisRoutes.AGENT_NEXUS)
//                        "consciousness" -> navController.navigate(GenesisRoutes.CONSCIOUSNESS_VISUALIZER)
//                        "trinity" -> navController.navigate(GenesisRoutes.TRINITY)
//                        "oracle" -> navController.navigate(GenesisRoutes.ORACLE_DRIVE)
//                    }
//                }
//            )
        }

        composable(GenesisRoutes.SETTINGS) {
//            SettingsScreen(
//                onNavigateBack = { navController.popBackStack() }
//            )
        }

        composable(GenesisRoutes.PROFILE) {
//            ProfileScreen(
//                onNavigateToSettings = { navController.navigate(GenesisRoutes.SETTINGS) }
//            )
        }

        composable(GenesisRoutes.OVERLAY) {
//            OverlayScreen(
//                onNavigateBack = { navController.popBackStack() }
//            )
        }

        composable(GenesisRoutes.SUBSCRIPTION) {
//            SubscriptionScreen(
//                onNavigateBack = { navController.popBackStack() }
//            )
        }
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
