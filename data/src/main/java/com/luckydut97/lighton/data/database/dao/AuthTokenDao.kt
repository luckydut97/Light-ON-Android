package com.luckydut97.lighton.data.database.dao

import androidx.room.*
import com.luckydut97.lighton.data.database.entity.AuthTokenEntity

/**
 * 인증 토큰 데이터 접근 객체
 */
@Dao
interface AuthTokenDao {

    /**
     * 현재 인증 토큰 조회
     */
    @Query("SELECT * FROM auth_tokens WHERE id = 1 LIMIT 1")
    suspend fun getCurrentToken(): AuthTokenEntity?

    /**
     * 인증 토큰 저장
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(token: AuthTokenEntity)

    /**
     * 인증 토큰 삭제 (로그아웃 시)
     */
    @Query("DELETE FROM auth_tokens")
    suspend fun deleteAllTokens()

    /**
     * 토큰이 만료되었는지 확인
     */
    @Query("SELECT COUNT(*) > 0 FROM auth_tokens WHERE id = 1 AND expiresAt > :currentTime")
    suspend fun isTokenValid(currentTime: Long = System.currentTimeMillis()): Boolean
}