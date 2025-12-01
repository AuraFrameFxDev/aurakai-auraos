package dev.aurakai.auraframefx.ui.overlays

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.overlayDataStore by preferencesDataStore(name = "overlay_settings")

object OverlayPrefs {
    private val KEY_ENABLED = booleanPreferencesKey("enabled")
    private val KEY_ORDER = stringPreferencesKey("order")

    fun enabledFlow(context: Context): Flow<Boolean> =
        context.overlayDataStore.data.map { it[KEY_ENABLED] ?: true }

    fun orderFlow(context: Context): Flow<List<String>> =
        context.overlayDataStore.data.map { prefs ->
            prefs[KEY_ORDER]?.split('|')?.filter { it.isNotBlank() }
                ?: listOf("Vignette", "Agent Edge", "Aura Presence", "Chat Bubble", "Sidebar")
        }

    suspend fun saveEnabled(context: Context, enabled: Boolean) {
        context.overlayDataStore.edit { it[KEY_ENABLED] = enabled }
    }

    suspend fun saveOrder(context: Context, order: List<String>) {
        context.overlayDataStore.edit { it[KEY_ORDER] = order.joinToString("|") }
    }
}

