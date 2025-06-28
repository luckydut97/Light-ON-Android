package com.luckydut97.lighton.feature_auth.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckydut97.domain.model.User
import com.luckydut97.domain.usecase.GetOAuthUrlUseCase
import com.luckydut97.domain.usecase.HandleOAuthCallbackUseCase
import com.luckydut97.lighton.data.repository.AuthRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 소셜 로그인 UI 상태
 */
data class SocialLoginUiState(
    val isLoading: Boolean = false,
    val authUrl: String? = null,
    val isSuccess: Boolean = false,
    val needsPersonalInfo: Boolean = false,
    val temporaryUserId: Int? = null,
    val user: User? = null,
    val errorMessage: String? = null
)

/**
 * 소셜 로그인 ViewModel
 */
class SocialLoginViewModel : ViewModel() {
    // 임시: 직접 의존성 주입 (DI 없이)
    private val repository = AuthRepositoryImpl()
    private val getOAuthUrlUseCase = GetOAuthUrlUseCase(repository)
    private val handleOAuthCallbackUseCase = HandleOAuthCallbackUseCase(repository)

    private val _uiState = MutableStateFlow(SocialLoginUiState())
    val uiState: StateFlow<SocialLoginUiState> = _uiState.asStateFlow()

    /**
     * 소셜 로그인 시작 - OAuth URL 가져오기
     */
    fun startSocialLogin(provider: String) {
        viewModelScope.launch {
            _uiState.value = SocialLoginUiState(isLoading = true)

            println("🚀 소셜 로그인 시작")
            println("   - 제공자: $provider")

            getOAuthUrlUseCase(provider).collect { result ->
                result.fold(
                    onSuccess = { authUrl ->
                        println("✅ OAuth URL 획득 성공")
                        println("   - URL: $authUrl")
                        _uiState.value = SocialLoginUiState(
                            authUrl = authUrl
                        )
                    },
                    onFailure = { exception ->
                        println("❌ OAuth URL 획득 실패")
                        println("   - 에러: ${exception.message}")
                        _uiState.value = SocialLoginUiState(
                            errorMessage = exception.message ?: "OAuth URL을 가져올 수 없습니다."
                        )
                    }
                )
            }
        }
    }

    /**
     * OAuth 콜백 처리
     */
    fun handleOAuthCallback(provider: String, code: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            println("🚀 OAuth 콜백 처리 시작")
            println("   - 제공자: $provider")
            println("   - 인가 코드: ${code.take(10)}...")

            handleOAuthCallbackUseCase(provider, code).collect { result ->
                result.fold(
                    onSuccess = { callbackResult ->
                        println("✅ OAuth 콜백 처리 성공")
                        println("   - 액세스 토큰: ${callbackResult.accessToken?.take(20)}...")
                        println("   - 사용자 ID: ${callbackResult.userId}")
                        println("   - 임시 사용자 ID: ${callbackResult.temporaryUserId}")

                        when {
                            // 로그인 성공 (기존 회원 + 개인정보 완료)
                            callbackResult.accessToken != null && callbackResult.userId != null -> {
                                println("📋 기존 회원 로그인 완료")
                                _uiState.value = SocialLoginUiState(
                                    isSuccess = true,
                                    user = User(
                                        id = callbackResult.userId.toString(),
                                        email = "temp@example.com", // TODO: 실제 사용자 정보 가져오기
                                        name = "임시사용자",
                                        profileImageUrl = null,
                                        phoneNumber = null,
                                        preferredGenres = emptyList(),
                                        preferredRegion = null
                                    )
                                )
                            }

                            // 개인정보 입력 필요 (기존/신규 회원)
                            callbackResult.temporaryUserId != null -> {
                                println("📝 개인정보 입력 필요")
                                _uiState.value = SocialLoginUiState(
                                    needsPersonalInfo = true,
                                    temporaryUserId = callbackResult.temporaryUserId
                                )
                            }

                            else -> {
                                println("❓ 알 수 없는 응답")
                                _uiState.value = SocialLoginUiState(
                                    errorMessage = "알 수 없는 응답입니다."
                                )
                            }
                        }
                    },
                    onFailure = { exception ->
                        println("❌ OAuth 콜백 처리 실패")
                        println("   - 에러: ${exception.message}")
                        _uiState.value = SocialLoginUiState(
                            errorMessage = exception.message ?: "소셜 로그인에 실패했습니다."
                        )
                    }
                )
            }
        }
    }

    /**
     * 에러 상태 초기화
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    /**
     * 상태 초기화
     */
    fun resetState() {
        _uiState.value = SocialLoginUiState()
    }
}