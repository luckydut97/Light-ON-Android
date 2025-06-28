package com.luckydut97.lighton.data.local

import android.content.Context
import android.content.SharedPreferences

/**
 * JWT 토큰 저장 및 관리 클래스
 */
class TokenManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "lighton_tokens"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"

        @Volatile
        private var INSTANCE: TokenManager? = null

        fun getInstance(context: Context): TokenManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: TokenManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    /**
     * 토큰 저장
     */
    fun saveTokens(
        accessToken: String,
        refreshToken: String,
        userId: String? = null,
        userEmail: String? = null
    ) {
        prefs.edit().apply {
            putString(KEY_ACCESS_TOKEN, accessToken)
            putString(KEY_REFRESH_TOKEN, refreshToken)
            userId?.let { putString(KEY_USER_ID, it) }
            userEmail?.let { putString(KEY_USER_EMAIL, it) }
            apply()
        }
    }

    /**
     * Access Token 가져오기
     */
    fun getAccessToken(): String? = prefs.getString(KEY_ACCESS_TOKEN, null)

    /**
     * Refresh Token 가져오기
     */
    fun getRefreshToken(): String? = prefs.getString(KEY_REFRESH_TOKEN, null)

    /**
     * 사용자 ID 가져오기
     */
    fun getUserId(): String? = prefs.getString(KEY_USER_ID, null)

    /**
     * 사용자 이메일 가져오기
     */
    fun getUserEmail(): String? = prefs.getString(KEY_USER_EMAIL, null)

    /**
     * 로그인 상태 확인
     */
    fun isLoggedIn(): Boolean = !getAccessToken().isNullOrEmpty()

    /**
     * 토큰 삭제 (로그아웃)
     */
    fun clearTokens() {
        prefs.edit().apply {
            remove(KEY_ACCESS_TOKEN)
            remove(KEY_REFRESH_TOKEN)
            remove(KEY_USER_ID)
            remove(KEY_USER_EMAIL)
            apply()
        }
    }

    /**
     * Authorization 헤더용 토큰 반환
     */
    fun getAuthorizationHeader(): String? {
        return getAccessToken()?.let { "Bearer $it" }
    }
}