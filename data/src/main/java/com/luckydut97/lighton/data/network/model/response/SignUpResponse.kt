package com.luckydut97.lighton.data.network.model.response

import com.google.gson.annotations.SerializedName

/**
 * 이메일 중복 확인 응답 데이터
 */
data class EmailDuplicateCheckResponseData(
    @SerializedName("isDuplicated")
    val isDuplicated: Boolean
)

/**
 * 이메일 중복 확인 응답 DTO
 * API 명세: GET /api/members/duplicate-check?email=xxx@xxx.com
 */
typealias EmailDuplicateCheckResponse = BaseResponse<EmailDuplicateCheckResponseData>

/**
 * 회원가입 응답 데이터
 */
data class SignUpResponseData(
    @SerializedName("temporaryUserId")
    val temporaryUserId: Int
)

/**
 * 회원가입 응답 DTO
 * API 명세: POST /api/members
 */
typealias SignUpResponse = BaseResponse<SignUpResponseData>

/**
 * 소셜 로그인 OAuth URL 응답 데이터
 * API 명세: GET /api/oauth/{provider}
 */
data class OAuthUrlResponseData(
    @SerializedName("authUrl")
    val authUrl: String
)

/**
 * OAuth URL 응답 DTO
 */
typealias OAuthUrlResponse = BaseResponse<OAuthUrlResponseData>

/**
 * 소셜 로그인 콜백 응답 데이터 (다양한 케이스)
 * API 명세: GET /api/oauth/{provider}/callback
 */
data class SocialLoginCallbackResponseData(
    // 로그인 성공 시
    @SerializedName("accessToken")
    val accessToken: String? = null,
    @SerializedName("refreshToken")
    val refreshToken: String? = null,
    @SerializedName("userId")
    val userId: Int? = null,

    // 회원가입 필요 시
    @SerializedName("isRegistered")
    val isRegistered: Boolean? = null,
    @SerializedName("temporaryUserId")
    val temporaryUserId: Int? = null
)

/**
 * 소셜 로그인 콜백 응답 DTO
 */
typealias SocialLoginCallbackResponse = BaseResponse<SocialLoginCallbackResponseData>

/**
 * 소셜 로그인 응답 타입 별칭 (기존 API용)
 */
typealias SocialLoginResponse = BaseResponse<LoginResponseData>

/**
 * 소셜 회원가입 응답 타입 별칭 (기존 API용)
 */
typealias SocialSignUpResponse = BaseResponse<SignUpResponseData>

/**
 * 개인정보 입력 완료 응답 타입 별칭
 */
typealias CompletePersonalInfoResponse = BaseResponse<LoginResponseData>
