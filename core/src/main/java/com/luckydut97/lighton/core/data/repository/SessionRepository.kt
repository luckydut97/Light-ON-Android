package com.luckydut97.lighton.core.data.repository

import kotlinx.coroutines.flow.StateFlow

data class ApiResponse<T>(
    val success: Boolean,
    val response: T? = null,
    val error: ErrorResponse? = null
)

data class ErrorResponse(
    val status: Int,
    val message: String
)

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)

sealed class AuthState {
    object Loading : AuthState()
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
}

interface SessionRepository {
    val authState: StateFlow<AuthState>
    suspend fun refreshTokens(): ApiResponse<TokenResponse>
    suspend fun isLoggedIn(): Boolean
    suspend fun logout(): ApiResponse<Any>
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun checkAuthStatus() // 인증 상태 확인, Splash 등에서 내부적으로 호출됨
}