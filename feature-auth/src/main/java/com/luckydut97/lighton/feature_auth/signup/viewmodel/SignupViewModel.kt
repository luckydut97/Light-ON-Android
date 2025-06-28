package com.luckydut97.lighton.feature_auth.signup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckydut97.domain.model.User
import com.luckydut97.domain.usecase.SignUpUseCase
import com.luckydut97.domain.usecase.CheckEmailDuplicateUseCase
import com.luckydut97.lighton.data.repository.AuthRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 회원가입 UI 상태
 */
data class SignupUiState(
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val user: User? = null,
    val temporaryUserId: String? = null,
    // 이메일 중복 확인 관련
    val emailCheckResult: EmailCheckResult? = null,
    val isEmailChecked: Boolean = false
)

/**
 * 이메일 중복 확인 결과
 */
sealed class EmailCheckResult {
    object Available : EmailCheckResult()      // 사용 가능
    object Duplicated : EmailCheckResult()     // 중복됨
    data class Error(val message: String) : EmailCheckResult()  // 에러
}

class SignupViewModel : ViewModel() {
    // 임시: 직접 의존성 주입 (DI 없이)
    private val repository = AuthRepositoryImpl()
    private val signUpUseCase = SignUpUseCase(repository)
    private val checkEmailDuplicateUseCase = CheckEmailDuplicateUseCase(repository)

    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState.asStateFlow()

    /**
     * 이메일 중복 확인
     */
    fun checkEmailDuplicate(email: String) {
        viewModelScope.launch {
            // 이메일 중복 확인 상태 초기화
            _uiState.value = _uiState.value.copy(
                emailCheckResult = null,
                isEmailChecked = false
            )

            println("🚀 이메일 중복 확인 시작 - 이메일: $email")

            checkEmailDuplicateUseCase(email).collect { result ->
                result.fold(
                    onSuccess = { isDuplicated ->
                        println("✅ 이메일 중복 확인 성공 - 중복 여부: $isDuplicated")
                        val checkResult = if (isDuplicated) {
                            EmailCheckResult.Duplicated
                        } else {
                            EmailCheckResult.Available
                        }
                        _uiState.value = _uiState.value.copy(
                            emailCheckResult = checkResult,
                            isEmailChecked = !isDuplicated
                        )
                    },
                    onFailure = { exception ->
                        println("❌ 이메일 중복 확인 실패 - 에러: ${exception.message}")
                        _uiState.value = _uiState.value.copy(
                            emailCheckResult = EmailCheckResult.Error(
                                exception.message ?: "중복 확인 중 오류가 발생했습니다."
                            ),
                            isEmailChecked = false
                        )
                    }
                )
            }
        }
    }

    /**
     * 회원가입
     */
    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            // 에러 메시지 초기화
            _uiState.value = _uiState.value.copy(errorMessage = null)

            println("🚀 회원가입 API 호출 시작 - 이메일: $email")

            signUpUseCase(email, password).collect { result ->
                result.fold(
                    onSuccess = { user ->
                        println("✅ 회원가입 성공 - 사용자 ID: ${user.id}")
                        _uiState.value = SignupUiState(
                            isSuccess = true,
                            user = user,
                            temporaryUserId = user.id
                        )
                    },
                    onFailure = { exception ->
                        println("❌ 회원가입 실패 - 에러: ${exception.message}")
                        _uiState.value = _uiState.value.copy(
                            errorMessage = exception.message ?: "회원가입에 실패했습니다."
                        )
                    }
                )
            }
        }
    }

    /**
     * 이메일 변경 시 중복 확인 상태 초기화
     */
    fun resetEmailCheck() {
        _uiState.value = _uiState.value.copy(
            emailCheckResult = null,
            isEmailChecked = false
        )
    }

    /**
     * 에러 메시지 초기화
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
