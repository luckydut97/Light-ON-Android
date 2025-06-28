package com.luckydut97.lighton.feature_auth.signup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import com.luckydut97.domain.model.User
import com.luckydut97.domain.usecase.CompletePersonalInfoUseCase
import com.luckydut97.domain.usecase.PersonalInfoData
import com.luckydut97.lighton.data.repository.AuthRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 개인정보 입력 UI 상태
 */
data class PersonalInfoUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val user: User? = null
)



/**
 * 개인정보 입력 완료 ViewModel
 */
class PersonalInfoViewModel : ViewModel() {
    // 임시: 직접 의존성 주입 (DI 없이)
    private val completePersonalInfoUseCase = CompletePersonalInfoUseCase(AuthRepositoryImpl())

    private val _uiState = MutableStateFlow(PersonalInfoUiState())
    val uiState: StateFlow<PersonalInfoUiState> = _uiState.asStateFlow()

    /**
     * 개인정보 입력 완료 처리
     */
    fun completePersonalInfo(temporaryUserId: Int, personalInfo: PersonalInfoData) {
        val tag = "🔍 디버깅: PersonalInfoViewModel"

        viewModelScope.launch {
            _uiState.value = PersonalInfoUiState(isLoading = true)

            Log.d(tag, "🚀 개인정보 입력 API 호출 시작")
            Log.d(tag, "  - 임시 사용자 ID: $temporaryUserId")
            Log.d(tag, "  - 이름: ${personalInfo.name}")
            Log.d(tag, "  - 전화번호: ${personalInfo.phone}")
            Log.d(tag, "  - 지역 코드: ${personalInfo.regionCode}")

            completePersonalInfoUseCase(temporaryUserId, personalInfo).collect { result ->
                result.fold(
                    onSuccess = { user ->
                        Log.d(tag, "✅ 개인정보 입력 성공!")
                        Log.d(tag, "  - 사용자 ID: ${user.id}")
                        Log.d(tag, "  - 이름: ${user.name}")
                        Log.d(tag, "  - 전화번호: ${user.phoneNumber}")
                        _uiState.value = PersonalInfoUiState(
                            isSuccess = true,
                            user = user
                        )
                    },
                    onFailure = { exception ->
                        Log.e(tag, "❌ 개인정보 입력 실패")
                        Log.e(tag, "  - 에러: ${exception.message}")
                        _uiState.value = PersonalInfoUiState(
                            errorMessage = exception.message ?: "개인정보 입력에 실패했습니다."
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
     * UI 상태 초기화
     */
    fun resetState() {
        _uiState.value = PersonalInfoUiState()
    }
}
