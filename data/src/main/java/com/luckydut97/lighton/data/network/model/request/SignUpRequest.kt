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