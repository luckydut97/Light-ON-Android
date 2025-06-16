package com.luckydut97.lighton.data.repository

import com.luckydut97.lighton.data.network.NetworkModule
import com.luckydut97.lighton.data.network.model.request.LoginRequest
import com.luckydut97.lighton.data.network.model.response.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Flow<Result<LoginResponse>>
}

class AuthRepositoryImpl : AuthRepository {
    private val authApi = NetworkModule.authApi

    override suspend fun login(email: String, password: String): Flow<Result<LoginResponse>> =
        flow {
            try {
                val response = authApi.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    response.body()?.let { loginResponse ->
                        if (loginResponse.success) {
                            emit(Result.success(loginResponse))
                        } else {
                            // 서버에서 실패 응답을 보낸 경우
                            val errorMessage = loginResponse.error?.message ?: "로그인에 실패했습니다."
                            emit(Result.failure(Exception(errorMessage)))
                        }
                    } ?: emit(Result.failure(Exception("응답 본문이 비어있습니다.")))
                } else {
                    // HTTP 에러 응답
                    emit(Result.failure(Exception("로그인 실패: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
