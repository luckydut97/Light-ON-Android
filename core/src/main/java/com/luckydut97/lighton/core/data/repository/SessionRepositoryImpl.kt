package com.luckydut97.lighton.core.data.repository

import android.util.Log
import com.luckydut97.lighton.core.data.storage.TokenManager
import com.luckydut97.lighton.core.data.storage.TokenSaver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SessionRepositoryImpl(
    private val tokenManager: TokenManager
) : SessionRepository, TokenSaver {

    private val tag = "🔍 디버깅: SessionRepository"

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    override val authState: StateFlow<AuthState> = _authState.asStateFlow()

    override suspend fun refreshTokens(): ApiResponse<TokenResponse> {
        Log.d(tag, "=== 토큰 재발급 시작 ===")

        val refreshToken = tokenManager.getRefreshToken()
        if (refreshToken == null) {
            Log.e(tag, "Refresh token이 없습니다.")
            return ApiResponse(
                success = false,
                error = ErrorResponse(
                    status = 401,
                    message = "No refresh token"
                )
            )
        }

        // TODO: 실제 API 호출은 나중에 구현
        Log.d(tag, "⚠️ TODO: 실제 토큰 재발급 API 호출 필요")

        // 임시로 성공 응답 반환 (개발용)
        return ApiResponse(
            success = true,
            response = TokenResponse(
                accessToken = "dummy_access_token",
                refreshToken = "dummy_refresh_token"
            )
        )
    }

    override suspend fun isLoggedIn(): Boolean {
        val loggedIn = tokenManager.isLoggedIn()
        Log.d(tag, "로그인 상태 확인: $loggedIn")
        return loggedIn
    }

    override suspend fun logout(): ApiResponse<Any> {
        Log.d(tag, "=== 로그아웃 처리 시작 ===")

        try {
            // TODO: 서버 로그아웃 API 호출
            Log.d(tag, "⚠️ TODO: 서버 로그아웃 API 호출 필요")

            // 로컬 토큰 삭제
            tokenManager.clearTokens()
            Log.d(tag, "💾 로컬 토큰 삭제 완료")

            // 인증 상태 업데이트
            _authState.value = AuthState.Unauthenticated
            Log.d(tag, "🔄 인증 상태를 Unauthenticated로 변경")

            return ApiResponse(success = true, response = null)
        } catch (e: Exception) {
            Log.e(tag, "🔥 로그아웃 예외: ${e.message}", e)
            return ApiResponse(
                success = false,
                error = ErrorResponse(
                    status = 0,
                    message = "로그아웃 처리 중 오류가 발생했습니다."
                )
            )
        }
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        Log.d(tag, "=== 토큰 저장 시작 ===")
        Log.d(tag, "액세스 토큰: ${accessToken.take(20)}...")
        Log.d(tag, "리프레시 토큰: ${refreshToken.take(20)}...")

        tokenManager.saveTokens(accessToken, refreshToken)
        _authState.value = AuthState.Authenticated

        Log.d(tag, "✅ 토큰 저장 완료 - 인증 상태로 변경")
        Log.d(tag, "현재 인증 상태: ${_authState.value}")
    }

    // 인증 상태 체크 메서드 (SplashViewModel에서 사용)
    override suspend fun checkAuthStatus() {
        Log.d(tag, "=== 인증 상태 확인 시작 ===")

        val isLoggedIn = isLoggedIn()
        Log.d(tag, "현재 로그인 상태: $isLoggedIn")

        if (isLoggedIn) {
            // 토큰이 있으면 재발급 시도
            Log.d(tag, "토큰이 존재함 - 토큰 재발급 시도")
            val refreshResult = refreshTokens()

            if (refreshResult.success) {
                Log.d(tag, "✅ 토큰 재발급 성공 - 인증된 상태로 변경")
                _authState.value = AuthState.Authenticated
            } else {
                Log.e(tag, "❌ 토큰 재발급 실패 - 로그아웃 후 비인증 상태로 변경")
                logout()
            }
        } else {
            Log.d(tag, "토큰이 없음 - 비인증 상태로 변경")
            _authState.value = AuthState.Unauthenticated
        }

        Log.d(tag, "인증 상태 확인 완료: ${_authState.value}")
    }
}