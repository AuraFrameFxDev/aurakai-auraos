package dev.aurakai.auraframefx.aura.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SentinelsFortressScreen() {
    val firewallEnabled = remember { mutableStateOf(false) }
    val vpnEnabled = remember { mutableStateOf(false) }
    val checksEnabled = remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Sentinel's Fortress",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Kai's security command center. Monitor and manage all security protocols from a single interface.",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Security toggles
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Firewall", modifier = Modifier.weight(1f))
            Switch(checked = firewallEnabled.value, onCheckedChange = { firewallEnabled.value = it })
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("VPN", modifier = Modifier.weight(1f))
            Switch(checked = vpnEnabled.value, onCheckedChange = { vpnEnabled.value = it })
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Security Checks", modifier = Modifier.weight(1f))
            Switch(checked = checksEnabled.value, onCheckedChange = { checksEnabled.value = it })
        }
    }
}
