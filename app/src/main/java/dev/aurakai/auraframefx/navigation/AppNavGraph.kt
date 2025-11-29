package dev.aurakai.auraframefx.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.aurakai.auraframefx.ui.gates.GateNavigationScreen
import dev.aurakai.auraframefx.navigation.NavDestination

import dev.aurakai.auraframefx.ui.screens.HomeScreen

/**
 * Main navigation graph for the AuraFrameFX app
 */
@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavDestination.Gates.route // Start with gate navigation
    ) {
        composable(
            route = NavDestination.Home.route
        ) {
            HomeScreen(navController = navController)
        }

        composable(
            route = NavDestination.Canvas.route
        ) {
//            CanvasScreen()
        }

        composable(
            route = NavDestination.AiChat.route
        ) {
//            AiChatScreen()
        }

        composable(
            route = NavDestination.Profile.route
        ) {
//            ProfileScreen()
        }

        // Duplicate Settings route removed here

        composable(
            route = NavDestination.OracleDriveControl.route
        ) {
            // Fixed: Use actual OracleDriveControlScreen instead of placeholder
//            dev.aurakai.auraframefx.ui.screens.oracledrive.OracleDriveControlScreen()
        }

        // Gate Navigation - Module Selection Hub
        composable(
            route = NavDestination.Gates.route
        ) {
            GateNavigationScreen(navController = navController)
        }

        // Journal PDA - Retro Gaming Wellness Hub
        composable(
            route = NavDestination.JournalPDA.route
        ) {
//            JournalPDAScreen(navController = navController)
        }

        // DataVein Hub - Entry point to data network visualization
        composable(
            route = NavDestination.DataVein.route
        ) {
//            dev.aurakai.auraframefx.datavein.ui.SimpleDataVeinScreen(
//                onLaunchSphereGrid = {
//                    navController.navigate(NavDestination.SphereGrid.route)
//                }
//            )
        }

        // DataVein Sphere Grid - FFX-style progression interface
        composable(
            route = NavDestination.SphereGrid.route
        ) {
//            dev.aurakai.auraframefx.datavein.ui.DataVeinSphereGrid(
//                onNodeSelected = { node ->
//                    // Log node selection for analytics and future detail screen navigation
//                    timber.log.Timber.i("DataVein node selected: ${node.id} - ${node.title}")
//                    // Future: navController.navigate("sphere_node_detail/${node.id}")
//                }
//            )
        }

        // Add AI Content navigation
        // aiContentNavigation() // Disabled for beta - AI content will be in main chat

        // Add more composable destinations as needed
    }
}
