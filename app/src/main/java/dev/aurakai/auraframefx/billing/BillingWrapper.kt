package dev.aurakai.auraframefx.billing

import androidx.compose.runtime.*
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * App-level billing wrapper
 *
 * Wraps entire app to enforce subscription rules:
 * - Shows paywall when trial expires
 * - Manages feature access throughout app
 */
@Composable
fun BillingWrapper(
    viewModel: SubscriptionViewModel = hiltViewModel(viewModelStoreOwner, key),
    content: @Composable () -> Unit
) {
    val subscriptionState by viewModel.subscriptionState.collectAsState()

    // Refresh subscription status on app start
    LaunchedEffect(Unit) {
        viewModel.refreshStatus()
    }

    // Show app content
    content()

    // Overlay paywall when trial expires
    if (subscriptionState is SubscriptionState.Free) {
        PaywallDialog(viewModel = viewModel)
    }
}
