package com.luckydut97.domain.usecase

import com.luckydut97.domain.model.User
import com.luckydut97.domain.repository.AuthRepository
import com.luckydut97.domain.repository.SocialProvider
import kotlinx.coroutines.flow.Flow

/**
 * 개인정보 데이터 (Domain Layer)
 */
data class PersonalInfoData(
    val name: String,
    val phone: String,
    val regionCode: Int,
    val agreeTerms: Boolean,
    val agreePrivacy: Boolean,
    val agreeOver14: Boolean,
    val agreeSMS: Boolean,
    val agreePush: Boolean,
    val agreeEmail: Boolean
)

/**
 * OAuth 콜백 결과 데이터
 */
data class OAuthCallbackResult(
    // 로그인 성공 시
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val userId: Int? = null,

    // 개인정보 입력 필요 시
    val temporaryUserId: Int? = null,
    val isRegistered: Boolean? = null
)

/**
 * 이메일 로그인 UseCase
 * 단일 책임: 이메일/패스워드 로그인 처리
 */
class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Flow<Result<User>> {
        return authRepository.login(email, password)
    }
}

/**
 * 이메일 중복 확인 UseCase
 * 단일 책임: 이메일 중복 여부 확인
 */
class CheckEmailDuplicateUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): Flow<Result<Boolean>> {
        return authRepository.checkEmailDuplicate(email)
    }
}

/**
 * 소셜 로그인 UseCase
 * 단일 책임: 소셜 플랫폼 로그인 처리
 */

/**
 * OAuth URL 가져오기 UseCase
 * 단일 책임: 소셜 로그인 인증 URL 가져오기
 */
class GetOAuthUrlUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(provider: String): Flow<Result<String>> {
        return authRepository.getOAuthUrl(provider)
    }
}

/**
 * OAuth 콜백 처리 UseCase
 * 단일 책임: 소셜 로그인 콜백 처리
 */
class HandleOAuthCallbackUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(provider: String, code: String): Flow<Result<OAuthCallbackResult>> {
        return authRepository.handleOAuthCallback(provider, code)
    }
}

/**
 * 소셜 로그인 UseCase
 * 단일 책임: 소셜 플랫폼 로그인 처리
 */
class SocialLoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        provider: SocialProvider,
        accessToken: String,
        email: String? = null
    ): Flow<Result<User>> {
        return authRepository.socialLogin(provider, accessToken, email)
    }
}

/**
 * 개인정보 입력 완료 UseCase
 * 단일 책임: 임시 회원의 개인정보 입력 완료 처리
 */
class CompletePersonalInfoUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        temporaryUserId: Int,
        personalInfo: PersonalInfoData
    ): Flow<Result<User>> {
        return authRepository.completePersonalInfo(temporaryUserId, personalInfo)
    }
}

/**
 * 현재 사용자 정보 가져오기 UseCase
 */
class GetCurrentUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Flow<User?> {
        return authRepository.getCurrentUser()
    }
}

/**
 * 로그아웃 UseCase
 */
class LogoutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.logout()
    }
}

/**
 * 이메일 회원가입 UseCase (API 명세에 맞게 간소화)
 */
class SignUpUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Flow<Result<User>> {
        return authRepository.signUp(email, password)
    }
}

/**
 * 소셜 회원가입 UseCase
 * 단일 책임: 소셜 플랫폼 회원가입 처리
 */
class SocialSignUpUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        provider: SocialProvider,
        accessToken: String,
        email: String,
        name: String,
        profileImageUrl: String? = null
    ): Flow<Result<User>> {
        return authRepository.socialSignUp(provider, accessToken, email, name, profileImageUrl)
    }
}
