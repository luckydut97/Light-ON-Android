package com.luckydut97.lighton.data.network.model.response

import com.google.gson.annotations.SerializedName
import retrofit2.Response

/**
 * API 공통 응답 래퍼
 */
data class BaseResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("response")
    val response: T?,
    @SerializedName("error")
    val error: ErrorResponse?
)

/**
 * 에러 응답 데이터
 */
data class ErrorResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String
) {
    fun toHttpStatus(): HttpStatus = HttpStatus.fromCode(status)
    fun toErrorType(): ErrorType = ErrorType.fromStatus(status)

    /**
     * 사용자에게 표시할 친화적인 에러 메시지
     */
    fun getUserFriendlyMessage(): String = when {
        // 인증 관련 에러 (401)
        message.contains("Authorization 헤더가 없습니다") -> "서버 설정에 문제가 있습니다. 관리자에게 문의해주세요."
        status == 401 -> "인증에 실패했습니다. 이메일과 비밀번호를 확인해주세요."

        // 로그인 관련 에러 (400번대)
        message.contains("이메일 형식") -> "올바른 이메일 형식을 입력해주세요."
        message.contains("비밀번호 형식") -> "비밀번호 형식을 확인해주세요."
        message.contains("비밀번호가 일치하지") -> "이메일 또는 비밀번호를 확인해주세요."
        message.contains("이메일 또는 비밀번호가 올바르지") -> "이메일 또는 비밀번호를 확인해주세요."
        message.contains("존재하지 않는 회원") -> "가입되지 않은 이메일입니다."
        message.contains("개인 정보를 입력") -> "개인정보 입력을 완료해주세요."
        message.contains("필수 항목이 누락") -> "이메일과 비밀번호를 모두 입력해주세요."

        // 회원가입 관련 에러
        message.contains("이미 존재하는 이메일") -> "이미 가입된 이메일입니다."

        // 서버 에러 (500번대)
        status >= 500 -> "서버에 일시적인 문제가 발생했습니다. 잠시 후 다시 시도해주세요."

        // 네트워크 에러
        status == -1 -> "네트워크 연결을 확인해주세요."

        // 기타
        else -> "오류가 발생했습니다. 다시 시도해주세요."
    }
}

/**
 * HTTP 상태 코드 정의
 */
enum class HttpStatus(val code: Int, val description: String) {
    OK(200, "OK"),
    CREATED(201, "Created"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    CONFLICT(409, "Conflict"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    UNKNOWN(-1, "Unknown");

    companion object {
        fun fromCode(code: Int): HttpStatus =
            values().find { it.code == code } ?: UNKNOWN
    }
}

/**
 * 에러 타입 분류
 */
enum class ErrorType {
    CLIENT_ERROR,    // 4xx
    SERVER_ERROR,    // 5xx
    NETWORK_ERROR,   // 네트워크 관련
    UNKNOWN_ERROR;   // 기타

    companion object {
        fun fromStatus(status: Int): ErrorType = when (status) {
            in 400..499 -> CLIENT_ERROR
            in 500..599 -> SERVER_ERROR
            else -> UNKNOWN_ERROR
        }
    }
}

/**
 * 로그인 응답 데이터
 */
data class LoginResponseData(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)

/**
 * 로그인 응답 타입 별칭
 */
typealias LoginResponse = BaseResponse<LoginResponseData>

/**
 * 공통 API 에러 처리 확장 함수
 */
fun <T> Result<T>.handleApiError(): Result<T> = this.onFailure { throwable ->
    when (throwable) {
        is retrofit2.HttpException -> {
            println("🌐 [HTTP 에러] ${throwable.code()}: ${throwable.message()}")
        }

        is java.io.IOException -> {
            println("🌐 [네트워크 에러] ${throwable.message}")
        }

        else -> {
            println("💥 [알 수 없는 에러] ${throwable.message}")
        }
    }
}
