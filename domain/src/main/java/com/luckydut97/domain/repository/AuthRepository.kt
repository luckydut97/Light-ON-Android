package com.luckydut97.domain.repository

import com.luckydut97.domain.model.User
import com.luckydut97.domain.usecase.PersonalInfoData
import com.luckydut97.domain.usecase.OAuthCallbackResult
import kotlinx.coroutines.flow.Flow

/**
 * 인증 관련 Domain Repository 인터페이스
 * Data Layer에서 이 인터페이스를 구현해야 함
 */
interface AuthRepository {
    /**
     * 이메일 로그인
     */
    suspend fun login(email: String, password: String): Flow<Result<User>>

    /**
     * 이메일 중복 확인
     */
    suspend fun checkEmailDuplicate(email: String): Flow<Result<Boolean>>

    /**
     * OAuth URL 가져오기
     * 소셜 로그인을 위한 인증 URL 반환
     */
    suspend fun getOAuthUrl(provider: String): Flow<Result<String>>

    /**
     * OAuth 콜백 처리
     * 인가 코드를 받아서 소셜 로그인 결과 반환
     */
    suspend fun handleOAuthCallback(
        provider: String,
        code: String
    ): Flow<Result<OAuthCallbackResult>>

    /**
     * 소셜 로그인
     */
    suspend fun socialLogin(
        provider: SocialProvider,
        accessToken: String,
        email: String? = null
    ): Flow<Result<User>>

    /**
     * 현재 로그인된 사용자 정보 가져오기
     */
    suspend fun getCurrentUser(): Flow<User?>

    /**
     * 로그아웃
     */
    suspend fun logout(): Result<Unit>

    /**
     * 이메일 회원가입 (API 명세에 맞게 이메일, 비밀번호만)
     */
    suspend fun signUp(
        email: String,
        password: String
    ): Flow<Result<User>>

    /**
     * 소셜 회원가입
     */
    suspend fun socialSignUp(
        provider: SocialProvider,
        accessToken: String,
        email: String,
        name: String,
        profileImageUrl: String? = null
    ): Flow<Result<User>>

    /**
     * 개인정보 입력 완료
     * 임시 회원이 필수 개인정보를 입력하여 정회원으로 전환
     */
    suspend fun completePersonalInfo(
        temporaryUserId: Int,
        personalInfo: PersonalInfoData
    ): Flow<Result<User>>
}

/**
 * 소셜 로그인 제공자 (Domain Layer)
 */
enum class SocialProvider {
    KAKAO,
    GOOGLE,
    NAVER
}
