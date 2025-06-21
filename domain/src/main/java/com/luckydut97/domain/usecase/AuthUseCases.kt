package com.luckydut97.domain.usecase

import com.luckydut97.domain.model.User
import com.luckydut97.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

/**
 * 로그인 UseCase
 * 단일 책임: 사용자 로그인 처리
 */
class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Flow<Result<User>> {
        return authRepository.login(email, password)
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
 * 회원가입 UseCase
 */
class SignUpUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        name: String,
        phoneNumber: String,
        preferredGenres: List<String> = emptyList(),
        preferredRegion: String? = null
    ): Flow<Result<User>> {
        return authRepository.signUp(
            email = email,
            password = password,
            name = name,
            phoneNumber = phoneNumber,
            preferredGenres = preferredGenres,
            preferredRegion = preferredRegion
        )
    }
}