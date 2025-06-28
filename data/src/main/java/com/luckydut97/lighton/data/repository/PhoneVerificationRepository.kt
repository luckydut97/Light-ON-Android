package com.luckydut97.lighton.data.repository

import android.util.Log
import com.luckydut97.lighton.data.network.model.request.PhoneVerificationRequest
import com.luckydut97.lighton.data.network.model.request.PhoneVerificationCodeRequest
import com.luckydut97.lighton.data.network.model.response.BaseResponse
import com.luckydut97.lighton.data.network.model.response.ErrorResponse
import com.luckydut97.lighton.data.network.NetworkModule

class PhoneVerificationRepository {

    private val tag = "🔍 디버깅: PhoneVerificationRepo"
    private val authApi = NetworkModule.authApi

    suspend fun requestVerificationCode(phoneNumber: String): BaseResponse<Any> {
        Log.d(tag, "=== 실제 인증번호 요청 API 호출 시작 ===")
        Log.d(tag, "입력받은 전화번호: $phoneNumber")
        Log.d(tag, "Endpoint: api/auth/phones/code")
        Log.d(tag, "HTTP Method: POST")
        Log.d(tag, "Content-Type: application/json")

        val request = PhoneVerificationRequest(phoneNumber = phoneNumber)

        Log.d(tag, "Request Body 생성:")
        Log.d(tag, "  phoneNumber: ${request.phoneNumber}")
        Log.d(tag, "Request Body JSON: {\"phoneNumber\": \"${request.phoneNumber}\"}")

        return try {
            Log.d(tag, "Retrofit API 호출 시작...")

            val response = authApi.requestPhoneVerification(request)

            Log.d(tag, "HTTP Status Code: ${response.code()}")
            Log.d(tag, "HTTP Status Message: ${response.message()}")
            Log.d(tag, "Response Headers: ${response.headers()}")

            if (response.isSuccessful) {
                val body = response.body()
                Log.d(tag, "인증번호 요청 API 호출 성공!")
                Log.d(tag, "Response Body: $body")

                body ?: BaseResponse(
                    success = false,
                    response = null,
                    error = ErrorResponse(
                        status = 500,
                        message = "응답 본문이 비어있습니다."
                    )
                )
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(tag, "인증번호 요청 API 호출 실패!")
                Log.e(tag, "Error Code: ${response.code()}")
                Log.e(tag, "Error Message: ${response.message()}")
                Log.e(tag, "Error Body: $errorBody")

                BaseResponse(
                    success = false,
                    response = null,
                    error = ErrorResponse(
                        status = response.code(),
                        message = when (response.code()) {
                            400 -> "전화번호가 올바르지 않습니다."
                            else -> "서버 오류가 발생했습니다."
                        }
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(tag, "네트워크 예외 발생: ${e.message}", e)
            BaseResponse(
                success = false,
                response = null,
                error = ErrorResponse(
                    status = -1,
                    message = "네트워크 오류: ${e.message}"
                )
            )
        } finally {
            Log.d(tag, "=== 실제 인증번호 요청 API 호출 완료 ===")
        }
    }

    suspend fun verifyPhoneCode(
        phoneNumber: String,
        code: String
    ): BaseResponse<String> {
        Log.d(tag, "=== 인증번호 확인 API 호출 시작 ===")
        Log.d(tag, "입력받은 전화번호: $phoneNumber")
        Log.d(tag, "입력받은 인증번호: $code")
        Log.d(tag, "Endpoint: api/auth/phones/code/verify")
        Log.d(tag, "HTTP Method: POST")
        Log.d(tag, "Content-Type: application/json")

        val request = PhoneVerificationCodeRequest(phoneNumber = phoneNumber, code = code)

        Log.d(tag, "Request Body 생성:")
        Log.d(tag, "  phoneNumber: ${request.phoneNumber}")
        Log.d(tag, "  code: ${request.code}")
        Log.d(
            tag,
            "Request Body JSON: {\"phoneNumber\": \"${request.phoneNumber}\", \"code\": \"${request.code}\"}"
        )

        return try {
            Log.d(tag, "Retrofit API 호출 시작...")

            val response = authApi.verifyPhoneCode(request)

            Log.d(tag, "HTTP Status Code: ${response.code()}")
            Log.d(tag, "HTTP Status Message: ${response.message()}")
            Log.d(tag, "Response Headers: ${response.headers()}")

            if (response.isSuccessful) {
                val body = response.body()
                Log.d(tag, "인증번호 확인 API 호출 성공!")
                Log.d(tag, "Response Body: $body")

                body ?: BaseResponse(
                    success = false,
                    response = null,
                    error = ErrorResponse(
                        status = 500,
                        message = "응답 본문이 비어있습니다."
                    )
                )
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(tag, "인증번호 확인 API 호출 실패!")
                Log.e(tag, "Error Code: ${response.code()}")
                Log.e(tag, "Error Message: ${response.message()}")
                Log.e(tag, "Error Body: $errorBody")

                BaseResponse<String>(
                    success = false,
                    response = null,
                    error = ErrorResponse(
                        status = response.code(),
                        message = when (response.code()) {
                            400 -> "아직 휴대폰 인증이 완료되지 않았습니다."
                            else -> "서버 오류: ${response.code()} - ${response.message()}"
                        }
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(tag, "네트워크 예외 발생: ${e.message}", e)
            BaseResponse(
                success = false,
                response = null,
                error = ErrorResponse(
                    status = -1,
                    message = "네트워크 오류: ${e.message}"
                )
            )
        } finally {
            Log.d(tag, "=== 인증번호 확인 API 호출 완료 ===")
        }
    }
}