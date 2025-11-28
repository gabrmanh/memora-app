package br.edu.ifsp.memora_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifsp.memora_app.data.local.dao.UserDao
import br.edu.ifsp.memora_app.data.remote.api.ApiClient
import br.edu.ifsp.memora_app.data.remote.api.AuthApi
import br.edu.ifsp.memora_app.data.remote.request.LoginRequest
import br.edu.ifsp.memora_app.data.remote.request.RegisterRequest
import br.edu.ifsp.memora_app.domain.user.User
import br.edu.ifsp.memora_app.ui.config.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val userDao: UserDao
) : ViewModel() {
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(SessionManager.getCurrentUser() != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val userDto = AuthApi.login(LoginRequest(email, password))
                val user = User(
                    id = userDto.id,
                    name = userDto.name,
                    email = userDto.email
                )

                userDao.insert(user)
                SessionManager.login(user, email, password)
                _isLoggedIn.value = true
                _uiState.value = AuthUiState.Success
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val userDto = AuthApi.register(RegisterRequest(name, email, password))
                val user = User(
                    id = userDto.id,
                    name = userDto.name,
                    email = userDto.email
                )

                userDao.insert(user)
                ApiClient.setBasicAuth(email, password)
                SessionManager.login(user, email, password)
                _isLoggedIn.value = true
                _uiState.value = AuthUiState.Success
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: "Registration failed")
            }
        }
    }

    fun logout() {
        SessionManager.logout()
        _isLoggedIn.value = false
    }

    fun clearError() {
        _uiState.value = AuthUiState.Idle
    }
}



sealed class AuthUiState {
    data object Idle : AuthUiState()
    data object Loading : AuthUiState()
    data object Success : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}
