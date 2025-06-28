package com.luckydut97.lighton.data.network.model.request

import com.google.gson.annotations.SerializedName

/**
 * 회원가입 요청 DTO
 * API 명세: POST /api/members
 */
data class SignUpRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)

/**
 * 개인정보 입력 완료 요청 DTO
 * API 명세: POST /api/members/{temporaryMemberId}/info
 */
data class PersonalInfoRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("regionCode")
    val regionCode: Int,
    @SerializedName("agreements")
    val agreements: AgreementRequest
)

/**
 * 약관 동의 정보
 */
data class AgreementRequest(
    @SerializedName("terms")
    val terms: Boolean,
    @SerializedName("privacy")
    val privacy: Boolean,
    @SerializedName("over14")
    val over14: Boolean,
    @SerializedName("marketing")
    val marketing: MarketingAgreementRequest
)

/**
 * 마케팅 동의 정보
 */
data class MarketingAgreementRequest(
    @SerializedName("sms")
    val sms: Boolean,
    @SerializedName("push")
    val push: Boolean,
    @SerializedName("email")
    val email: Boolean
)

/**
 * 소셜 로그인 요청 DTO
 * API 명세: POST /api/members/social-login
 */
data class SocialLoginRequest(
    @SerializedName("provider")
    val provider: SocialProvider,
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("email")
    val email: String? = null
)

/**
 * 소셜 회원가입 요청 DTO
 * API 명세: POST /api/members/social-signup
 */
data class SocialSignUpRequest(
    @SerializedName("provider")
    val provider: SocialProvider,
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("profileImageUrl")
    val profileImageUrl: String? = null
)

/**
 * 소셜 로그인 제공자
 */
enum class SocialProvider {
    @SerializedName("KAKAO")
    KAKAO,

    @SerializedName("GOOGLE")
    GOOGLE,

    @SerializedName("NAVER")
    NAVER
}