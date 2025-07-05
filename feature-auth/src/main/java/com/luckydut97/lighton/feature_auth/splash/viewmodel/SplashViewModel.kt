package com.luckydut97.lighton.feature_auth.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _navigateToMain = MutableStateFlow(false)
    val navigateToMain: StateFlow<Boolean> = _navigateToMain.asStateFlow()

    init {
        viewModelScope.launch {
            // 토큰 체크 및 사용자 정보 로드 로직
            checkTokenAndLoadUserInfo()
            
            // 스플래시 화면 표시 시간
            delay(2000)
            
            _isLoading.value = false
            _navigateToMain.value = true
        }
    }
    
    private suspend fun checkTokenAndLoadUserInfo() {
        // TODO: 실제 토큰 체크 로직 구현
        // 1. SharedPreferences에서 토큰 확인
        // 2. 토큰이 있으면 사용자 정보 조회 API 호출
        // 3. 사용자 정보를 전역 상태에 저장 (UserState 업데이트)
        
        // 임시로 간단한 로직만 구현
        println("🔍 토큰 체크 및 사용자 정보 로드 중...")
        delay(500) // 네트워크 요청 시뮬레이션
        println("✅ 토큰 체크 완료 - 무조건 메인 화면으로 이동")
    }
}