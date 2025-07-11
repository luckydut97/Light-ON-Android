package com.luckydut97.lighton.data.storage

interface TokenManager {
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun clearTokens()
    suspend fun isLoggedIn(): Boolean
}