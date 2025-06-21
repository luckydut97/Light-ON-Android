package com.luckydut97.domain.repository

import com.luckydut97.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * 인증 관련 Domain Repository 인터페이스
 * Data Layer에서 이 인터페이스를 구현해야 함
 */
interface AuthRepository {
    /**
     * 사용자 로그인
     */
    suspend fun login(email: String, password: String): Flow<Result<User>>

    /**
     * 현재 로그인된 사용자 정보 가져오기
     */
    suspend fun getCurrentUser(): Flow<User?>

    /**
     * 로그아웃
     */
    suspend fun logout(): Result<Unit>

    /**
     * 회원가입
     */
    suspend fun signUp(
        email: String,
        password: String,
        name: String,
        phoneNumber: String,
        preferredGenres: List<String> = emptyList(),
        preferredRegion: String? = null
    ): Flow<Result<User>>
}