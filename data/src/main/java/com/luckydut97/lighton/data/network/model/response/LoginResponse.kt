package com.luckydut97.lighton.data.network.model.response

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("response")
    val response: T?,
    @SerializedName("error")
    val error: ErrorResponse?
)

data class ErrorResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String
)

data class LoginResponseData(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)

// 타입 별칭으로 LoginResponse 정의
typealias LoginResponse = BaseResponse<LoginResponseData>
