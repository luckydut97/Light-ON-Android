package com.luckydut97.lighton.feature_auth.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckydut97.lighton.data.repository.AuthRepository
import com.luckydut97.lighton.data.repository.AuthRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null
)

class LoginViewModel : ViewModel() {
    private val authRepository: AuthRepository = AuthRepositoryImpl()

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState(isLoading = true)

            authRepository.login(email, password).collect { result ->
                result.fold(
                    onSuccess = { response ->
                        // 성공 응답 처리
                        response.response?.let { loginData ->
                            _uiState.value = LoginUiState(
                                isSuccess = true,
                                accessToken = loginData.accessToken,
                                refreshToken = loginData.refreshToken
                            )
                        } ?: run {
                            _uiState.value = LoginUiState(
                                errorMessage = "로그인 응답이 비어있습니다."
                            )
                        }
                    },
                    onFailure = { exception ->
                        _uiState.value = LoginUiState(
                            errorMessage = exception.message ?: "네트워크 오류가 발생했습니다."
                        )
                    }
                )
            }
        }
    }
}
