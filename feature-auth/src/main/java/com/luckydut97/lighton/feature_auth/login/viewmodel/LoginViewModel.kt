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
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val user: User? = null,
    val isNetworkError: Boolean = false,
    val isValidationError: Boolean = false
)

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)

class LoginViewModel : ViewModel() {
    // 임시: 직접 의존성 주입 (DI 없이)
    private val loginUseCase = LoginUseCase(AuthRepositoryImpl())

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult(false, "이메일을 입력해주세요.")
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                ValidationResult(false, "올바른 이메일 형식을 입력해주세요.")

            else -> ValidationResult(true)
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult(false, "비밀번호를 입력해주세요.")
            password.length < 6 -> ValidationResult(false, "비밀번호는 6자 이상 입력해주세요.")
            else -> ValidationResult(true)
        }
    }

    fun login(email: String, password: String) {
        // 입력 유효성 검사
        val emailValidation = validateEmail(email)
        if (!emailValidation.isValid) {
            _uiState.value = LoginUiState(
                errorMessage = emailValidation.errorMessage,
                isValidationError = true
            )
            return
        }

        val passwordValidation = validatePassword(password)
        if (!passwordValidation.isValid) {
            _uiState.value = LoginUiState(
                errorMessage = passwordValidation.errorMessage,
                isValidationError = true
            )
            return
        }

        viewModelScope.launch {
            println("🚀 로그인 API 호출 시작")
            println("   - 이메일: $email")

            loginUseCase(email, password).collect { result ->
                result.fold(
                    onSuccess = { user ->
                        println("✅ 로그인 성공!")
                        println("   - 사용자 ID: ${user.id}")
                        println("   - 이메일: ${user.email}")
                        println("   - 이름: ${user.name}")
                        _uiState.value = LoginUiState(
                            isSuccess = true,
                            user = user
                        )
                    },
                    onFailure = { exception ->
                        println("❌ 로그인 실패")
                        println("   - 에러: ${exception.message}")

                        val isNetworkError = exception.message?.contains("네트워크") == true ||
                                exception.message?.contains("연결") == true

                        _uiState.value = LoginUiState(
                            errorMessage = exception.message ?: "로그인 중 오류가 발생했습니다.",
                            isNetworkError = isNetworkError
                        )
                    }
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            isNetworkError = false,
            isValidationError = false
        )
    }

    fun resetState() {
        _uiState.value = LoginUiState()
    }
}