package dev.aurakai.auraframefx.cascade.trinity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.aurakai.auraframefx.models.UserData
import dev.aurakai.auraframefx.models.AgentRequest
import dev.aurakai.auraframefx.models.AgentResponse
import dev.aurakai.auraframefx.models.Theme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface TrinityUiState {
    data object Loading : TrinityUiState
    data class Error(val message: String) : TrinityUiState
    data object Processing : TrinityUiState
    data class Success(
        val user: UserData? = null,
        val agentStatus: Map<String, AgentResponse> = emptyMap(),
        val availableThemes: List<Theme> = emptyList(),
        val lastAgentResponse: AgentResponse? = null,
        val lastAgentType: String? = null
    ) : TrinityUiState
}

@HiltViewModel
class TrinityViewModel @Inject constructor(
    // private val repository: TrinityRepository // Commented out until Repository is fixed
) : ViewModel() {

    private val _uiState = MutableStateFlow<TrinityUiState>(TrinityUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = TrinityUiState.Loading
            
            // Simulate loading for now until Repository is ready
            // loadUserData()
            // loadAgentStatus()
            // loadThemes()
            
            _uiState.value = TrinityUiState.Success(
                user = UserData(name = "User", role = "Admin", username = "User"),
                agentStatus = emptyMap(),
                availableThemes = emptyList()
            )
        }
    }

    fun applyTheme(themeId: String) {
        viewModelScope.launch {
            // repository.applyTheme(themeId)
        }
    }

    fun processAgentRequest(agentType: String, request: Map<String, Any>) {
        viewModelScope.launch {
            _uiState.value = TrinityUiState.Processing
            // repository.processAgentRequest(...)
            // Simulate success for now
            val currentState = _uiState.value
            if (currentState is TrinityUiState.Success) {
                 _uiState.value = currentState // No-op for now
            } else {
                 _uiState.value = TrinityUiState.Success()
            }
        }
    }

    fun refresh() {
        loadInitialData()
    }
}
