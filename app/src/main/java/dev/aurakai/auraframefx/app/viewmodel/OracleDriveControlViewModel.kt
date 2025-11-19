package dev.aurakai.auraframefx.app.viewmodel

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel for controlling and monitoring the Oracle Drive Xposed service.
 */
@HiltViewModel
class OracleDriveControlViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _isServiceConnected = MutableStateFlow(false)
    val isServiceConnected: StateFlow<Boolean> = _isServiceConnected.asStateFlow()

    private val _status = MutableStateFlow("Disconnected")
    val status: StateFlow<String> = _status.asStateFlow()

    private val _detailedStatus = MutableStateFlow("")
    val detailedStatus: StateFlow<String> = _detailedStatus.asStateFlow()

    private val _diagnosticsLog = MutableStateFlow("")
    val diagnosticsLog: StateFlow<String> = _diagnosticsLog.asStateFlow()

    private var serviceBinder: IOracleDriveService? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Timber.d("Oracle Drive service connected")
            serviceBinder = IOracleDriveService.Stub.asInterface(service)
            _isServiceConnected.value = true
            viewModelScope.launch { refreshStatus() }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Timber.w("Oracle Drive service disconnected")
            serviceBinder = null
            _isServiceConnected.value = false
            _status.value = "Disconnected"
        }
    }

    fun bindService() {
        try {
            val intent = Intent().apply {
                component = ComponentName(
                    "dev.aurakai.auraframefx",
                    "dev.aurakai.auraframefx.oracledrive.OracleDriveService"
                )
            }
            val bound = context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            if (bound) {
                addLog("Binding to Oracle Drive service...")
            } else {
                addLog("ERROR: Failed to bind to Oracle Drive service")
                _status.value = "Bind Failed"
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to bind Oracle Drive service")
            addLog("ERROR: ${e.message}")
            _status.value = "Error: ${e.message}"
        }
    }

    fun unbindService() {
        try {
            if (_isServiceConnected.value) {
                context.unbindService(serviceConnection)
                serviceBinder = null
                _isServiceConnected.value = false
                _status.value = "Disconnected"
                addLog("Unbound from Oracle Drive service")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error unbinding Oracle Drive service")
            addLog("ERROR unbinding: ${e.message}")
        }
    }

    suspend fun refreshStatus() {
        val binder = serviceBinder ?: run {
            _status.value = "Not Connected"
            _detailedStatus.value = "Service not bound"
            addLog("Cannot refresh: service not connected")
            return
        }

        try {
            val serviceStatus = binder.getStatus()
            _status.value = serviceStatus ?: "Unknown"

            val detailed = binder.getDetailedStatus()
            _detailedStatus.value = detailed ?: "No detailed status available"

            addLog("Status refreshed: $serviceStatus")
        } catch (e: Exception) {
            Timber.e(e, "Failed to refresh Oracle Drive status")
            _status.value = "Error: ${e.message}"
            addLog("ERROR refreshing status: ${e.message}")
        }
    }

    suspend fun toggleModule(packageName: String, enable: Boolean) {
        val binder = serviceBinder ?: run {
            addLog("ERROR: Cannot toggle module - service not connected")
            throw IllegalStateException("Service not connected")
        }

        try {
            val action = if (enable) "Enabling" else "Disabling"
            addLog("$action module: $packageName")

            val result = binder.toggleModule(packageName, enable)
            if (result) {
                addLog("SUCCESS: Module $packageName ${if (enable) "enabled" else "disabled"}")
            } else {
                addLog("FAILED: Could not toggle module $packageName")
            }

            refreshStatus()
        } catch (e: Exception) {
            Timber.e(e, "Failed to toggle module $packageName")
            addLog("ERROR toggling module $packageName: ${e.message}")
            throw e
        }
    }

    private fun addLog(message: String) {
        val timestamp = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault())
            .format(java.util.Date())
        _diagnosticsLog.value = "${_diagnosticsLog.value}[$timestamp] $message\n"
    }

    override fun onCleared() {
        super.onCleared()
        unbindService()
    }
}

interface IOracleDriveService {
    fun getStatus(): String?
    fun getDetailedStatus(): String?
    fun toggleModule(packageName: String, enable: Boolean): Boolean

    object Stub {
        fun asInterface(binder: IBinder?): IOracleDriveService? {
            return null // TODO: Implement AIDL binding
        }
    }
}
