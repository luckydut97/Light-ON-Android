package com.luckydut97.lighton.feature_auth.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckydut97.domain.model.User
import com.luckydut97.domain.usecase.LoginUseCase
import com.luckydut97.lighton.data.repository.AuthRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val user: User? = null
)

class LoginViewModel : ViewModel() {
    // 임시: 직접 의존성 주입 (DI 없이)
    private val loginUseCase = LoginUseCase(AuthRepositoryImpl())

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState(isLoading = true)

            loginUseCase(email, password).collect { result ->
                result.fold(
                    onSuccess = { user ->
                        _uiState.value = LoginUiState(
                            isSuccess = true,
                            user = user
                        )
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
