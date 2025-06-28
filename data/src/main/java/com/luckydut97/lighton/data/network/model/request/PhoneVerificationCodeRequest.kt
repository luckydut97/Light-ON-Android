package com.luckydut97.lighton.data.network.model.request

/**
 * 휴대폰 본인인증 요청 모델
 */
data class PhoneVerificationCodeRequest(
    val phoneNumber: String,
    val code: String
)