package com.luckydut97.lighton.data.repository

import com.luckydut97.lighton.data.network.NetworkModule
import com.luckydut97.lighton.data.network.model.request.LoginRequest
import com.luckydut97.lighton.data.network.model.request.SignUpRequest
import com.luckydut97.lighton.data.network.model.request.SocialLoginRequest
import com.luckydut97.lighton.data.network.model.request.SocialSignUpRequest
import com.luckydut97.lighton.data.network.model.request.PersonalInfoRequest
import com.luckydut97.lighton.data.network.model.request.AgreementRequest
import com.luckydut97.domain.model.User
import com.luckydut97.domain.repository.AuthRepository
import com.luckydut97.domain.usecase.PersonalInfoData
import com.luckydut97.domain.usecase.OAuthCallbackResult
import com.luckydut97.lighton.data.network.model.request.MarketingAgreementRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import retrofit2.HttpException
import retrofit2.Response
import com.luckydut97.domain.repository.SocialProvider as DomainSocialProvider
import com.luckydut97.lighton.data.network.model.request.SocialProvider as DataSocialProvider

class AuthRepositoryImpl : AuthRepository {
    private val authApi = NetworkModule.authApi

    override suspend fun login(email: String, password: String): Flow<Result<User>> = flow {
        try {
            println("🚀 [API] 로그인 요청 전송")
            println("   - URL: POST ${NetworkModule.getBaseUrl()}api/members/login")
            println("   - 이메일: $email")

            val response = authApi.login(LoginRequest(email, password))

            println("📡 [API] 로그인 응답 수신")
            println("   - HTTP 상태: ${response.code()}")

            if (response.isSuccessful) {
                // 성공 응답 처리
                response.body()?.let { loginResponse ->
                    println("📄 [API] 성공 응답 본문 파싱")
                    println("   - success: ${loginResponse.success}")

                    if (loginResponse.success && loginResponse.response != null) {
                        val loginData = loginResponse.response

                        // TODO: 토큰 저장 로직 (나중에 추가)
                        println("🔐 [토큰] 저장 준비")
                        println("   - 액세스 토큰: ${loginData.accessToken.take(20)}...")
                        println("   - 리프레시 토큰: ${loginData.refreshToken.take(20)}...")

                        val user = User(
                            id = "temp_id", // TODO: JWT에서 추출
                            email = email,
                            name = "temp_name", // TODO: 사용자 정보 API 호출
                            profileImageUrl = null,
                            phoneNumber = null,
                            preferredGenres = emptyList(),
                            preferredRegion = null
                        )
                        emit(Result.success(user))
                    } else {
                        println("❓ [API] 성공 상태이지만 데이터 없음")
                        emit(Result.failure(Exception("알 수 없는 오류가 발생했습니다.")))
                    }
                } ?: run {
                    println("❌ [API] 성공 응답 본문이 비어있음")
                    emit(Result.failure(Exception("응답 본문이 비어있습니다.")))
                }
            } else {
                // 에러 응답 처리 - errorBody() 사용
                response.errorBody()?.let { errorBody ->
                    try {
                        val errorJson = errorBody.string()
                        println("📄 [API] 에러 응답 본문 파싱")
                        println("   - 원본 JSON: $errorJson")

                        // JSON을 직접 파싱하여 에러 메시지 추출
                        val gson = com.google.gson.Gson()
                        val errorResponse = gson.fromJson(
                            errorJson,
                            com.luckydut97.lighton.data.network.model.response.LoginResponse::class.java
                        )

                        if (errorResponse?.error != null) {
                            println("⚠️ [API] 서버 에러 메시지 추출 성공")
                            println("   - 상태: ${errorResponse.error.status}")
                            println("   - 메시지: ${errorResponse.error.message}")
                            emit(Result.failure(Exception(errorResponse.error.getUserFriendlyMessage())))
                        } else {
                            println("❓ [API] 에러 응답 구조가 예상과 다름")
                            emit(Result.failure(Exception("서버 오류가 발생했습니다.")))
                        }
                    } catch (e: Exception) {
                        println("💥 [API] 에러 응답 파싱 실패")
                        println("   - 파싱 에러: ${e.message}")
                        emit(Result.failure(Exception("로그인 실패: ${response.code()}")))
                    }
                } ?: run {
                    println("❌ [API] 에러 응답 본문도 비어있음")
                    emit(Result.failure(Exception("로그인 실패: ${response.code()}")))
                }
            }
        } catch (e: IOException) {
            println("🌐 [네트워크] 연결 오류")
            println("   - 에러: ${e.message}")
            emit(Result.failure(Exception("네트워크 연결 오류가 발생했습니다.")))
        } catch (e: Exception) {
            println("💥 [예외] 예상치 못한 오류")
            println("   - 에러: ${e.message}")
            emit(Result.failure(Exception("로그인 중 오류가 발생했습니다: ${e.message}")))
        }
    }

    override suspend fun getOAuthUrl(provider: String): Flow<Result<String>> = flow {
        try {
            val response = authApi.getOAuthUrl(provider)
            if (response.isSuccessful) {
                response.body()?.let { oauthResponse ->
                    if (oauthResponse.success && oauthResponse.response != null) {
                        emit(Result.success(oauthResponse.response.authUrl))
                    } else if (oauthResponse.error != null) {
                        emit(Result.failure(Exception(oauthResponse.error.message)))
                    } else {
                        emit(Result.failure(Exception("OAuth URL을 가져올 수 없습니다.")))
                    }
                } ?: emit(Result.failure(Exception("응답 본문이 비어있습니다.")))
            } else {
                emit(Result.failure(Exception("OAuth URL 요청 실패: ${response.code()}")))
            }
        } catch (e: IOException) {
            emit(Result.failure(Exception("네트워크 연결 오류가 발생했습니다.")))
        } catch (e: Exception) {
            emit(Result.failure(Exception("OAuth URL 요청 중 오류가 발생했습니다: ${e.message}")))
        }
    }

    override suspend fun handleOAuthCallback(
        provider: String,
        code: String
    ): Flow<Result<OAuthCallbackResult>> = flow {
        try {
            val response = authApi.handleOAuthCallback(provider, code)
            if (response.isSuccessful) {
                response.body()?.let { callbackResponse ->
                    if (callbackResponse.success && callbackResponse.response != null) {
                        val data = callbackResponse.response
                        val result = OAuthCallbackResult(
                            accessToken = data.accessToken,
                            refreshToken = data.refreshToken,
                            userId = data.userId,
                            temporaryUserId = data.temporaryUserId,
                            isRegistered = data.isRegistered
                        )
                        emit(Result.success(result))
                    } else if (callbackResponse.error != null) {
                        emit(Result.failure(Exception(callbackResponse.error.message)))
                    } else {
                        emit(Result.failure(Exception("OAuth 콜백 처리에 실패했습니다.")))
                    }
                } ?: emit(Result.failure(Exception("응답 본문이 비어있습니다.")))
            } else {
                emit(Result.failure(Exception("OAuth 콜백 처리 실패: ${response.code()}")))
            }
        } catch (e: IOException) {
            emit(Result.failure(Exception("네트워크 연결 오류가 발생했습니다.")))
        } catch (e: Exception) {
            emit(Result.failure(Exception("OAuth 콜백 처리 중 오류가 발생했습니다: ${e.message}")))
        }
    }

    override suspend fun socialLogin(
        provider: DomainSocialProvider,
        accessToken: String,
        email: String?
    ): Flow<Result<User>> = flow {
        try {
            val request = SocialLoginRequest(
                provider = provider.toDataSocialProvider(),
                accessToken = accessToken,
                email = email
            )
            val response = authApi.socialLogin(request)
            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    if (loginResponse.success && loginResponse.response != null) {
                        val loginData = loginResponse.response

                        // TODO: 토큰 저장 로직
                        println("소셜 로그인 토큰: ${loginData.accessToken}")

                        val user = User(
                            id = "temp_id",
                            email = email ?: "unknown@example.com",
                            name = "temp_name", // TODO: 소셜 로그인 응답에서 사용자 정보 가져오기
                            profileImageUrl = null,
                            phoneNumber = null,
                            preferredGenres = emptyList(),
                            preferredRegion = null
                        )
                        emit(Result.success(user))
                    } else if (loginResponse.error != null) {
                        emit(Result.failure(Exception(loginResponse.error.message)))
                    } else {
                        emit(Result.failure(Exception("알 수 없는 오류가 발생했습니다.")))
                    }
                } ?: emit(Result.failure(Exception("응답 본문이 비어있습니다.")))
            } else {
                // 에러 응답 처리 - errorBody() 사용
                response.errorBody()?.let { errorBody ->
                    try {
                        val errorJson = errorBody.string()
                        println("📄 [API] 에러 응답 본문 파싱")
                        println("   - 원본 JSON: $errorJson")

                        // JSON을 직접 파싱하여 에러 메시지 추출
                        val gson = com.google.gson.Gson()
                        val errorResponse = gson.fromJson(
                            errorJson,
                            com.luckydut97.lighton.data.network.model.response.SocialLoginResponse::class.java
                        )

                        if (errorResponse?.error != null) {
                            println("⚠️ [API] 서버 에러 메시지 추출 성공")
                            println("   - 상태: ${errorResponse.error.status}")
                            println("   - 메시지: ${errorResponse.error.message}")
                            emit(Result.failure(Exception(errorResponse.error.message)))
                        } else {
                            println("❓ [API] 에러 응답 구조가 예상과 다름")
                            emit(Result.failure(Exception("서버 오류가 발생했습니다.")))
                        }
                    } catch (e: Exception) {
                        println("💥 [API] 에러 응답 파싱 실패")
                        println("   - 파싱 에러: ${e.message}")
                        emit(Result.failure(Exception("소셜 로그인 실패: ${response.code()}")))
                    }
                } ?: run {
                    println("❌ [API] 에러 응답 본문도 비어있음")
                    emit(Result.failure(Exception("소셜 로그인 실패: ${response.code()}")))
                }
            }
        } catch (e: IOException) {
            emit(Result.failure(Exception("네트워크 연결 오류가 발생했습니다.")))
        } catch (e: Exception) {
            emit(Result.failure(Exception("소셜 로그인 중 오류가 발생했습니다: ${e.message}")))
        }
    }

    override suspend fun signUp(email: String, password: String): Flow<Result<User>> = flow {
        try {
            println("🚀 [API] 회원가입 요청 전송")
            println("   - URL: POST ${NetworkModule.getBaseUrl()}api/members")
            println("   - 이메일: $email")

            val response = authApi.signUp(SignUpRequest(email, password))

            println("📡 [API] 회원가입 응답 수신")
            println("   - HTTP 상태: ${response.code()}")

            if (response.isSuccessful) {
                // 성공 응답 처리
                response.body()?.let { signUpResponse ->
                    println("📄 [API] 성공 응답 본문 파싱")
                    println("   - success: ${signUpResponse.success}")

                    if (signUpResponse.success && signUpResponse.response != null) {
                        println("✅ [회원가입] 성공!")
                        println("   - 임시 사용자 ID: ${signUpResponse.response.temporaryUserId}")

                        val user = User(
                            id = signUpResponse.response.temporaryUserId.toString(),
                            email = email,
                            name = "", // 회원가입 단계에서는 이름 정보 없음
                            profileImageUrl = null,
                            phoneNumber = null,
                            preferredGenres = emptyList(),
                            preferredRegion = null
                        )
                        emit(Result.success(user))
                    } else {
                        println("❓ [API] 성공 상태이지만 데이터 없음")
                        emit(Result.failure(Exception("알 수 없는 오류가 발생했습니다.")))
                    }
                } ?: run {
                    println("❌ [API] 성공 응답 본문이 비어있음")
                    emit(Result.failure(Exception("응답 본문이 비어있습니다.")))
                }
            } else {
                // 에러 응답 처리 - errorBody() 사용
                response.errorBody()?.let { errorBody ->
                    try {
                        val errorJson = errorBody.string()
                        println("📄 [API] 에러 응답 본문 파싱")
                        println("   - 원본 JSON: $errorJson")

                        // JSON을 직접 파싱하여 에러 메시지 추출
                        val gson = com.google.gson.Gson()
                        val errorResponse = gson.fromJson(
                            errorJson,
                            com.luckydut97.lighton.data.network.model.response.SignUpResponse::class.java
                        )

                        if (errorResponse?.error != null) {
                            println("⚠️ [API] 서버 에러 메시지 추출 성공")
                            println("   - 상태: ${errorResponse.error.status}")
                            println("   - 메시지: ${errorResponse.error.message}")
                            emit(Result.failure(Exception(errorResponse.error.getUserFriendlyMessage())))
                        } else {
                            println("❓ [API] 에러 응답 구조가 예상과 다름")
                            emit(Result.failure(Exception("서버 오류가 발생했습니다.")))
                        }
                    } catch (e: Exception) {
                        println("💥 [API] 에러 응답 파싱 실패")
                        println("   - 파싱 에러: ${e.message}")
                        emit(Result.failure(Exception("회원가입 실패: ${response.code()}")))
                    }
                } ?: run {
                    println("❌ [API] 에러 응답 본문도 비어있음")
                    emit(Result.failure(Exception("회원가입 실패: ${response.code()}")))
                }
            }
        } catch (e: IOException) {
            println("🌐 [네트워크] 연결 오류")
            println("   - 에러: ${e.message}")
            emit(Result.failure(Exception("네트워크 연결 오류가 발생했습니다.")))
        } catch (e: Exception) {
            println("💥 [예외] 예상치 못한 오류")
            println("   - 에러: ${e.message}")
            emit(Result.failure(Exception("회원가입 중 오류가 발생했습니다: ${e.message}")))
        }
    }

    override suspend fun socialSignUp(
        provider: DomainSocialProvider,
        accessToken: String,
        email: String,
        name: String,
        profileImageUrl: String?
    ): Flow<Result<User>> = flow {
        try {
            val request = SocialSignUpRequest(
                provider = provider.toDataSocialProvider(),
                accessToken = accessToken,
                email = email,
                name = name,
                profileImageUrl = profileImageUrl
            )
            val response = authApi.socialSignUp(request)
            if (response.isSuccessful) {
                response.body()?.let { signUpResponse ->
                    if (signUpResponse.success && signUpResponse.response != null) {
                        val user = User(
                            id = signUpResponse.response.temporaryUserId.toString(),
                            email = email,
                            name = name,
                            profileImageUrl = profileImageUrl,
                            phoneNumber = null,
                            preferredGenres = emptyList(),
                            preferredRegion = null
                        )
                        emit(Result.success(user))
                    } else if (signUpResponse.error != null) {
                        emit(Result.failure(Exception(signUpResponse.error.message)))
                    } else {
                        emit(Result.failure(Exception("알 수 없는 오류가 발생했습니다.")))
                    }
                } ?: emit(Result.failure(Exception("응답 본문이 비어있습니다.")))
            } else {
                // 에러 응답 처리 - errorBody() 사용
                response.errorBody()?.let { errorBody ->
                    try {
                        val errorJson = errorBody.string()
                        println("📄 [API] 에러 응답 본문 파싱")
                        println("   - 원본 JSON: $errorJson")

                        // JSON을 직접 파싱하여 에러 메시지 추출
                        val gson = com.google.gson.Gson()
                        val errorResponse = gson.fromJson(
                            errorJson,
                            com.luckydut97.lighton.data.network.model.response.SocialSignUpResponse::class.java
                        )

                        if (errorResponse?.error != null) {
                            println("⚠️ [API] 서버 에러 메시지 추출 성공")
                            println("   - 상태: ${errorResponse.error.status}")
                            println("   - 메시지: ${errorResponse.error.message}")
                            emit(Result.failure(Exception(errorResponse.error.message)))
                        } else {
                            println("❓ [API] 에러 응답 구조가 예상과 다름")
                            emit(Result.failure(Exception("서버 오류가 발생했습니다.")))
                        }
                    } catch (e: Exception) {
                        println("💥 [API] 에러 응답 파싱 실패")
                        println("   - 파싱 에러: ${e.message}")
                        emit(Result.failure(Exception("소셜 회원가입 실패: ${response.code()}")))
                    }
                } ?: run {
                    println("❌ [API] 에러 응답 본문도 비어있음")
                    emit(Result.failure(Exception("소셜 회원가입 실패: ${response.code()}")))
                }
            }
        } catch (e: IOException) {
            emit(Result.failure(Exception("네트워크 연결 오류가 발생했습니다.")))
        } catch (e: Exception) {
            emit(Result.failure(Exception("소셜 회원가입 중 오류가 발생했습니다: ${e.message}")))
        }
    }

    override suspend fun completePersonalInfo(
        temporaryUserId: Int,
        personalInfo: PersonalInfoData
    ): Flow<Result<User>> = flow {
        try {
            val request = PersonalInfoRequest(
                name = personalInfo.name,
                phone = personalInfo.phone,
                regionCode = personalInfo.regionCode,
                agreements = AgreementRequest(
                    terms = personalInfo.agreeTerms,
                    privacy = personalInfo.agreePrivacy,
                    over14 = personalInfo.agreeOver14,
                    marketing = MarketingAgreementRequest(
                        sms = personalInfo.agreeSMS,
                        push = personalInfo.agreePush,
                        email = personalInfo.agreeEmail
                    )
                )
            )

            val response = authApi.completePersonalInfo(temporaryUserId, request)
            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    if (loginResponse.success && loginResponse.response != null) {
                        val loginData = loginResponse.response

                        // TODO: 토큰 저장
                        println("개인정보 완료 - 액세스 토큰: ${loginData.accessToken}")
                        println("개인정보 완료 - 리프레시 토큰: ${loginData.refreshToken}")

                        val user = User(
                            id = temporaryUserId.toString(),
                            email = "temp@example.com", // TODO: 실제 사용자 이메일 가져오기
                            name = personalInfo.name,
                            profileImageUrl = null,
                            phoneNumber = personalInfo.phone,
                            preferredGenres = emptyList(),
                            preferredRegion = personalInfo.regionCode.toString() // TODO: 지역 코드를 지역명으로 변환
                        )
                        emit(Result.success(user))
                    } else if (loginResponse.error != null) {
                        emit(Result.failure(Exception(loginResponse.error.message)))
                    } else {
                        emit(Result.failure(Exception("알 수 없는 오류가 발생했습니다.")))
                    }
                } ?: emit(Result.failure(Exception("응답 본문이 비어있습니다.")))
            } else {
                // 에러 응답 처리 - errorBody() 사용
                response.errorBody()?.let { errorBody ->
                    try {
                        val errorJson = errorBody.string()
                        println("📄 [API] 에러 응답 본문 파싱")
                        println("   - 원본 JSON: $errorJson")

                        // JSON을 직접 파싱하여 에러 메시지 추출
                        val gson = com.google.gson.Gson()
                        val errorResponse = gson.fromJson(
                            errorJson,
                            com.luckydut97.lighton.data.network.model.response.CompletePersonalInfoResponse::class.java
                        )

                        if (errorResponse?.error != null) {
                            println("⚠️ [API] 서버 에러 메시지 추출 성공")
                            println("   - 상태: ${errorResponse.error.status}")
                            println("   - 메시지: ${errorResponse.error.message}")
                            emit(Result.failure(Exception(errorResponse.error.message)))
                        } else {
                            println("❓ [API] 에러 응답 구조가 예상과 다름")
                            emit(Result.failure(Exception("서버 오류가 발생했습니다.")))
                        }
                    } catch (e: Exception) {
                        println("💥 [API] 에러 응답 파싱 실패")
                        println("   - 파싱 에러: ${e.message}")
                        emit(Result.failure(Exception("개인정보 입력 실패: ${response.code()}")))
                    }
                } ?: run {
                    println("❌ [API] 에러 응답 본문도 비어있음")
                    emit(Result.failure(Exception("개인정보 입력 실패: ${response.code()}")))
                }
            }
        } catch (e: IOException) {
            emit(Result.failure(Exception("네트워크 연결 오류가 발생했습니다.")))
        } catch (e: Exception) {
            emit(Result.failure(Exception("개인정보 입력 중 오류가 발생했습니다: ${e.message}")))
        }
    }

    override suspend fun getCurrentUser(): Flow<User?> = flow {
        // TODO: 토큰 확인하여 현재 사용자 정보 반환
        emit(null)
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            // TODO: 토큰 삭제 및 서버 로그아웃 API 호출
            println("로그아웃 완료")
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun checkEmailDuplicate(email: String): Flow<Result<Boolean>> = flow {
        try {
            println("🚀 [API] 이메일 중복 확인 요청 전송")
            println("   - URL: GET ${NetworkModule.getBaseUrl()}api/members/duplicate-check?email=$email")

            val response = authApi.checkEmailDuplicate(email)

            println("📡 [API] 이메일 중복 확인 응답 수신")
            println("   - HTTP 상태: ${response.code()}")

            if (response.isSuccessful) {
                response.body()?.let { duplicateResponse ->
                    println("📄 [API] 성공 응답 본문 파싱")
                    println("   - success: ${duplicateResponse.success}")

                    if (duplicateResponse.success && duplicateResponse.response != null) {
                        val isDuplicated = duplicateResponse.response.isDuplicated
                        println("✅ [중복 확인] 성공!")
                        println("   - 중복 여부: $isDuplicated")
                        emit(Result.success(isDuplicated))
                    } else {
                        println("❓ [API] 성공 상태이지만 데이터 없음")
                        emit(Result.failure(Exception("중복 확인 중 오류가 발생했습니다.")))
                    }
                } ?: run {
                    println("❌ [API] 성공 응답 본문이 비어있음")
                    emit(Result.failure(Exception("응답 본문이 비어있습니다.")))
                }
            } else {
                // 에러 응답 처리 - errorBody() 사용
                response.errorBody()?.let { errorBody ->
                    try {
                        val errorJson = errorBody.string()
                        println("📄 [API] 에러 응답 본문 파싱")
                        println("   - 원본 JSON: $errorJson")

                        val gson = com.google.gson.Gson()
                        val errorResponse = gson.fromJson(
                            errorJson,
                            com.luckydut97.lighton.data.network.model.response.EmailDuplicateCheckResponse::class.java
                        )

                        if (errorResponse?.error != null) {
                            println("⚠️ [API] 서버 에러 메시지 추출 성공")
                            println("   - 상태: ${errorResponse.error.status}")
                            println("   - 메시지: ${errorResponse.error.message}")
                            emit(Result.failure(Exception(errorResponse.error.getUserFriendlyMessage())))
                        } else {
                            println("❓ [API] 에러 응답 구조가 예상과 다름")
                            emit(Result.failure(Exception("서버 오류가 발생했습니다.")))
                        }
                    } catch (e: Exception) {
                        println("💥 [API] 에러 응답 파싱 실패")
                        println("   - 파싱 에러: ${e.message}")
                        emit(Result.failure(Exception("중복 확인 실패: ${response.code()}")))
                    }
                } ?: run {
                    println("❌ [API] 에러 응답 본문도 비어있음")
                    emit(Result.failure(Exception("중복 확인 실패: ${response.code()}")))
                }
            }
        } catch (e: IOException) {
            println("🌐 [네트워크] 연결 오류")
            println("   - 에러: ${e.message}")
            emit(Result.failure(Exception("네트워크 연결 오류가 발생했습니다.")))
        } catch (e: Exception) {
            println("💥 [예외] 예상치 못한 오류")
            println("   - 에러: ${e.message}")
            emit(Result.failure(Exception("중복 확인 중 오류가 발생했습니다: ${e.message}")))
        }
    }

    /**
     * Domain SocialProvider를 Data SocialProvider로 변환
     */
    private fun DomainSocialProvider.toDataSocialProvider(): DataSocialProvider {
        return when (this) {
            DomainSocialProvider.KAKAO -> DataSocialProvider.KAKAO
            DomainSocialProvider.GOOGLE -> DataSocialProvider.GOOGLE
            DomainSocialProvider.NAVER -> DataSocialProvider.NAVER
        }
    }
}
