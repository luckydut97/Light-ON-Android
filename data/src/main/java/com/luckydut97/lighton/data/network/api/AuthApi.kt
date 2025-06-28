package com.luckydut97.lighton.data.network.api

import com.luckydut97.lighton.data.network.model.request.LoginRequest
import com.luckydut97.lighton.data.network.model.request.SocialLoginRequest
import com.luckydut97.lighton.data.network.model.request.SocialSignUpRequest
import com.luckydut97.lighton.data.network.model.request.PersonalInfoRequest
import com.luckydut97.lighton.data.network.model.request.PhoneVerificationRequest
import com.luckydut97.lighton.data.network.model.request.PhoneVerificationCodeRequest
import com.luckydut97.lighton.data.network.model.request.SignUpRequest
import com.luckydut97.lighton.data.network.model.response.LoginResponse
import com.luckydut97.lighton.data.network.model.response.SignUpResponse
import com.luckydut97.lighton.data.network.model.response.EmailDuplicateCheckResponse
import com.luckydut97.lighton.data.network.model.response.BaseResponse
import com.luckydut97.lighton.data.network.model.response.OAuthUrlResponse
import com.luckydut97.lighton.data.network.model.response.SocialLoginCallbackResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthApi {
    /**
     * 이메일 로그인
     * Swagger 명세: POST /api/auth/login
     */
    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    /**
     * 이메일 중복 확인
     * API 명세: GET /api/members/duplicate-check?email=xxx@xxx.com
     */
    @GET("api/members/duplicate-check")
    suspend fun checkEmailDuplicate(
        @Query("email") email: String
    ): Response<EmailDuplicateCheckResponse>

    /**
     * 이메일 회원가입
     */
    @POST("api/members")
    suspend fun signUp(
        @Body request: SignUpRequest
    ): Response<SignUpResponse>

    /**
     * 소셜 로그인 OAuth URL 가져오기
     * API 명세: GET /api/oauth/{provider}
     */
    @GET("api/oauth/{provider}")
    suspend fun getOAuthUrl(
        @Path("provider") provider: String
    ): Response<OAuthUrlResponse>

    /**
     * 소셜 로그인 콜백 처리
     * API 명세: GET /api/oauth/{provider}/callback?code=xyz123
     *
     * 주의: 이 API는 WebView에서 자동으로 호출되며,
     * 앱에서는 콜백 URL을 감지하여 code를 추출한 후 직접 호출할 수도 있습니다.
     */
    @GET("api/oauth/{provider}/callback")
    suspend fun handleOAuthCallback(
        @Path("provider") provider: String,
        @Query("code") code: String
    ): Response<SocialLoginCallbackResponse>

    /**
     * 소셜 로그인
     */
    @POST("api/members/social/login")
    suspend fun socialLogin(
        @Body request: SocialLoginRequest
    ): Response<LoginResponse>

    /**
     * 소셜 회원가입
     */
    @POST("api/members/social/signup")
    suspend fun socialSignUp(
        @Body request: SocialSignUpRequest
    ): Response<SignUpResponse>

    /**
     * 개인정보 입력 완료
     * API 명세: POST /api/members/{temporaryMemberId}/info
     */
    @POST("api/members/{temporaryMemberId}/info")
    suspend fun completePersonalInfo(
        @Path("temporaryMemberId") temporaryMemberId: Int,
        @Body request: PersonalInfoRequest
    ): Response<LoginResponse>

    /**
     * 휴대폰 인증번호 발송
     * API 명세: POST /api/auth/phones/code
     */
    @POST("api/auth/phones/code")
    suspend fun requestPhoneVerification(
        @Body request: PhoneVerificationRequest
    ): Response<BaseResponse<Any>>

    /**
     * 휴대폰 본인인증
     * API 명세: POST /api/auth/phones/code/verify
     */
    @POST("api/auth/phones/code/verify")
    suspend fun verifyPhoneCode(
        @Body request: PhoneVerificationCodeRequest
    ): Response<BaseResponse<String>>

    // 추후 추가될 API들
    /**
     * 로그아웃
     */
    // @POST("api/members/logout")
    // suspend fun logout(): Response<BaseResponse<Unit>>

    /**
     * 토큰 갱신
     */
    // @POST("api/members/refresh")
    // suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<LoginResponse>

    /**
     * 회원 탈퇴
     */
    // @DELETE("api/members")
    // suspend fun withdraw(): Response<BaseResponse<Unit>>
}
