package com.luckydut97.lighton.data.repository

import com.luckydut97.lighton.data.network.NetworkModule
import com.luckydut97.lighton.data.network.model.request.LoginRequest
import com.luckydut97.domain.model.User
import com.luckydut97.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl : AuthRepository {
    private val authApi = NetworkModule.authApi

    override suspend fun login(email: String, password: String): Flow<Result<User>> = flow {
        try {
            val response = authApi.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    if (loginResponse.success) {
                        // DTO를 Domain 모델로 변환
                        val user = User(
                            id = "temp_id", // TODO: 실제로는 response에서 가져와야 함
                            email = email,
                            name = "temp_name", // TODO: 실제로는 response에서 가져와야 함
                            profileImageUrl = null,
                            phoneNumber = null,
                            preferredGenres = emptyList(),
                            preferredRegion = null
                        )
                        emit(Result.success(user))
                    } else {
                        val errorMessage = loginResponse.error?.message ?: "로그인에 실패했습니다."
                        emit(Result.failure(Exception(errorMessage)))
                    }
                } ?: emit(Result.failure(Exception("응답 본문이 비어있습니다.")))
            } else {
                emit(Result.failure(Exception("로그인 실패: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun getCurrentUser(): Flow<User?> = flow {
        // TODO: 로컬 저장소에서 현재 사용자 정보 가져오기
        emit(null)
    }

    override suspend fun logout(): Result<Unit> {
        // TODO: 로그아웃 API 호출 및 로컬 데이터 정리
        return Result.success(Unit)
    }

    override suspend fun signUp(
        email: String,
        password: String,
        name: String,
        phoneNumber: String,
        preferredGenres: List<String>,
        preferredRegion: String?
    ): Flow<Result<User>> = flow {
        // TODO: 회원가입 API 구현
        emit(Result.failure(Exception("회원가입 기능은 아직 구현되지 않았습니다.")))
    }
}
