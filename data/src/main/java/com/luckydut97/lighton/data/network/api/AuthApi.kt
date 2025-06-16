package com.luckydut97.lighton.data.network.api

import com.luckydut97.lighton.data.network.model.request.LoginRequest
import com.luckydut97.lighton.data.network.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/members/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    // 추후 추가될 API들
    // @POST("api/members/signup")
    // suspend fun signup(@Body request: SignupRequest): Response<SignupResponse>

    // @POST("api/members/logout")
    // suspend fun logout(): Response<BaseResponse>

    // @POST("api/members/refresh")
    // suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<TokenResponse>
}
