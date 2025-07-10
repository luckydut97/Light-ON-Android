package com.luckydut97.lighton.feature_auth.splash.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.luckydut97.lighton.core.data.repository.AuthState
import com.luckydut97.lighton.core.data.repository.SessionRepository

class SplashViewModel(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val tag = "🔍 디버깅: SplashViewModel"

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        startAuthCheck()
    }

    fun startAuthCheck() {
        viewModelScope.launch {
            try {
                Log.d(tag, "=== 인증 상태 확인 시작 ===")
                // 2초 대기와 내부 인증 로직을 launch/async 등으로 동시에 처리 가능, UI 레이어에서 타이머 대기
                sessionRepository.checkAuthStatus()
                // SessionRepository의 authState를 collect 하여 상태 반영
                sessionRepository.authState.collect { state ->
                    _authState.value = state
                }
            } catch (e: kotlinx.coroutines.CancellationException) {
                // 화면 종료 등 정상 취소는 로그 남기지 않음
            } catch (e: Exception) {
                Log.e(tag, "인증 상태 확인 중 오류: ${e.message}", e)
                _authState.value = AuthState.Unauthenticated
            }
        }
    }
}