package com.luckydut97.lighton.data.repository

import com.luckydut97.lighton.data.network.NetworkModule
import com.luckydut97.lighton.data.network.model.request.LoginRequest
import com.luckydut97.lighton.data.network.model.request.SignUpRequest
import com.luckydut97.lighton.data.network.model.request.PersonalInfoRequest
import com.luckydut97.lighton.data.network.model.request.AgreementRequest
import com.luckydut97.lighton.data.network.model.request.SocialLoginRequest
import com.luckydut97.lighton.data.network.model.request.SocialSignUpRequest
import com.luckydut97.lighton.data.network.model.request.MarketingAgreementRequest
import com.luckydut97.domain.model.User
import com.luckydut97.domain.repository.AuthRepository
import com.luckydut97.domain.usecase.PersonalInfoData
import com.luckydut97.domain.usecase.OAuthCallbackResult
import com.luckydut97.domain.repository.SocialProvider as DomainSocialProvider
import com.luckydut97.lighton.data.network.model.request.SocialProvider as DataSocialProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import retrofit2.HttpException
import retrofit2.Response
import android.util.Log

class AuthRepositoryImpl : AuthRepository {
    private val authApi = NetworkModule.authApi
    private val tag = "🔍 디버깅: AuthRepositoryImpl"

    override suspend fun login(email: String, password: String): Flow<Result<User>> = flow {
        try {
            Log.d(tag, "🚀 [API] 로그인 요청 전송")
            Log.d(tag, "   - URL: POST ${NetworkModule.getBaseUrl()}api/members/login")
            Log.d(tag, "   - 이메일: $email")

            val response = authApi.login(LoginRequest(email, password))

            Log.d(tag, "📡 [API] 로그인 응답 수신")
            Log.d(tag, "   - HTTP 상태: ${response.code()}")

            if (response.isSuccessful) {
                // 성공 응답 처리
                response.body()?.let { loginResponse ->
                    Log.d(tag, "📄 [API] 성공 응답 본문 파싱")
                    Log.d(tag, "   - success: ${loginResponse.success}")

                    if (loginResponse.success && loginResponse.response != null) {
                        val loginData = loginResponse.response

                        Log.d(tag, "🔐 [토큰] 로그인 성공 - 토큰 정보")
                        Log.d(tag, "   - 액세스 토큰: ${loginData.accessToken.take(20)}...")
                        Log.d(tag, "   - 리프레시 토큰: ${loginData.refreshToken.take(20)}...")
                        Log.d(tag, "⚠️ [토큰] ViewModel 또는 상위 레이어에서 토큰 저장 필요")

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
                        Log.d(tag, "❓ [API] 성공 상태이지만 데이터 없음")
                        emit(Result.failure(Exception("알 수 없는 오류가 발생했습니다.")))
                    }
                } ?: run {
                    Log.d(tag, "❌ [API] 성공 응답 본문이 비어있음")
                    emit(Result.failure(Exception("응답 본문이 비어있습니다.")))
                }
            } else {
                // 에러 응답 처리 - errorBody() 사용
                response.errorBody()?.let { errorBody ->
                    try {
                        val errorJson = errorBody.string()
                        Log.d(tag, "📄 [API] 에러 응답 본문 파싱")
                        Log.d(tag, "   - 원본 JSON: $errorJson")

                        // JSON을 직접 파싱하여 에러 메시지 추출
                        val gson = com.google.gson.Gson()
                        val errorResponse = gson.fromJson(
                            errorJson,
                            com.luckydut97.lighton.data.network.model.response.LoginResponse::class.java
                        )

                        if (errorResponse?.error != null) {
                            Log.e(tag, "⚠️ [API] 서버 에러 메시지 추출 성공")
                            Log.e(tag, "   - 상태: ${errorResponse.error.status}")
                            Log.e(tag, "   - 메시지: ${errorResponse.error.message}")
                            emit(Result.failure(Exception(errorResponse.error.getUserFriendlyMessage())))
                        } else {
                            Log.e(tag, "❓ [API] 에러 응답 구조가 예상과 다름")
                            emit(Result.failure(Exception("서버 오류가 발생했습니다.")))
                        }
                    } catch (e: Exception) {
                        Log.e(tag, "💥 [API] 에러 응답 파싱 실패")
                        Log.e(tag, "   - 파싱 에러: ${e.message}")
                        emit(Result.failure(Exception("로그인 실패: ${response.code()}")))
                    }
                } ?: run {
                    Log.e(tag, "❌ [API] 에러 응답 본문도 비어있음")
                    emit(Result.failure(Exception("로그인 실패: ${response.code()}")))
                }
            }
        } catch (e: IOException) {
            Log.e(tag, "🌐 네트워크 연결 오류")
            Log.e(tag, "   - 에러: ${e.message}")
            emit(Result.failure(Exception("네트워크 연결 오류가 발생했습니다.")))
        } catch (e: Exception) {
            Log.e(tag, "💥 예상치 못한 오류 발생")
            Log.e(tag, "   - 에러: ${e.message}")
            Log.e(tag, "   - 스택 트레이스: ${e.stackTraceToString()}")
            emit(Result.failure(Exception("로그인 중 오류가 발생했습니다: ${e.message}")))
        }
    }

    override suspend fun getOAuthUrl(provider: String): Flow<Result<String>> = flow {
        val tag = "🔍 디버깅: AuthRepositoryImpl"
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
            Log.e(tag, "🌐 네트워크 연결 오류")
            Log.e(tag, "   - 에러: ${e.message}")
            emit(Result.failure(Exception("네트워크 연결 오류가 발생했습니다.")))
        } catch (e: Exception) {
            Log.e(tag, "💥 예상치 못한 오류 발생")
            Log.e(tag, "   - 에러: ${e.message}")
            Log.e(tag, "   - 스택 트레이스: ${e.stackTraceToString()}")
            emit(Result.failure(Exception("OAuth URL 요청 중 오류가 발생했습니다: ${e.message}")))
        }
    }

    override suspend fun handleOAuthCallback(
        provider: String,
        code: String
    ): Flow<Result<OAuthCallbackResult>> = flow {
        val tag = "🔍 디버깅: AuthRepositoryImpl"
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
            Log.e(tag, "🌐 네트워크 연결 오류")
            Log.e(tag, "   - 에러: ${e.message}")
            emit(Result.failure(Exception("네트워크 연결 오류가 발생했습니다.")))
        } catch (e: Exception) {
            Log.e(tag, "💥 예상치 못한 오류 발생")
            Log.e(tag, "   - 에러: ${e.message}")
            Log.e(tag, "   - 스택 트레이스: ${e.stackTraceToString()}")
            emit(Result.failure(Exception("OAuth 콜백 처리 중 오류가 발생했습니다: ${e.message}")))
        }
    }

    override suspend fun socialLogin(
        provider: DomainSocialProvider,
        accessToken: String,
        email: String?
    ): Flow<Result<User>> = flow {
        val tag = "🔍 디버깅: AuthRepositoryImpl"
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

                        Log.d(tag, "🔐 [토큰] 로그인 성공 - 토큰 정보")
                        Log.d(tag, "   - 액세스 토큰: ${loginData.accessToken.take(20)}...")
                        Log.d(tag, "   - 리프레시 토큰: ${loginData.refreshToken.take(20)}...")
                        Log.d(tag, "⚠️ [토큰] ViewModel 또는 상위 레이어에서 토큰 저장 필요")

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
                        Log.d(tag, "📄 [API] 에러 응답 본문 파싱")
                        Log.d(tag, "   - 원본 JSON: $errorJson")

                        // JSON을 직접 파싱하여 에러 메시지 추출
                        val gson = com.google.gson.Gson()
                        val errorResponse = gson.fromJson(
                            errorJson,
                            com.luckydut97.lighton.data.network.model.response.SocialLoginResponse::class.java
                        )

                        if (errorResponse?.error != null) {
                            Log.e(tag, "⚠️ [API] 서버 에러 메시지 추출 성공")
                            Log.e(tag, "   - 상태: ${errorResponse.error.status}")
                            Log.e(tag, "   - 메시지: ${errorResponse.error.message}")
                            emit(Result.failure(Exception(errorResponse.error.message)))
                        } else {
                            Log.e(tag, "❓ [API] 에러 응답 구조가 예상과 다름")
                            emit(Result.failure(Exception("서버 오류가 발생했습니다.")))
                        }
                    } catch (e: Exception) {
                        Log.e(tag, "💥 [API] 에러 응답 파싱 실패")
                        Log.e(tag, "   - 파싱 에러: ${e.message}")
                        emit(Result.failure(Exception("소셜 로그인 실패: ${response.code()}")))
                    }
                } ?: run {
                    Log.e(tag, "❌ [API] 에러 응답 본문도 비어있음")
                    emit(Result.failure(Exception("소셜 로그인 실패: ${response.code()}")))
                }
            }
        } catch (e: IOException) {
            Log.e(tag, "🌐 네트워크 연결 오류")
            Log.e(tag, "   - 에러: ${e.message}")
            emit(Result.failure(Exception("네트워크 연결 오류가 발생했습니다.")))
        } catch (e: Exception) {
            Log.e(tag, "💥 예상치 못한 오류 발생")
            Log.e(tag, "   - 에러: ${e.message}")
            Log.e(tag, "   - 스택 트레이스: ${e.stackTraceToString()}")
            emit(Result.failure(Exception("소셜 로그인 중 오류가 발생했습니다: ${e.message}")))
        }
    }

    override suspend fun signUp(email: String, password: String): Flow<Result<User>> = flow {
        val tag = "🔍 디버깅: AuthRepositoryImpl"
        try {
            Log.d(tag, "🚀 [API] 회원가입 요청 전송")
            Log.d(tag, "   - URL: POST ${NetworkModule.getBaseUrl()}api/members")
            Log.d(tag, "   - 이메일: $email")

            val response = authApi.signUp(SignUpRequest(email, password))

            Log.d(tag, "📡 [API] 회원가입 응답 수신")
            Log.d(tag, "   - HTTP 상태: ${response.code()}")

            if (response.isSuccessful) {
                // 성공 응답 처리
                response.body()?.let { signUpResponse ->
                    Log.d(tag, "📄 [API] 성공 응답 본문 파싱")
                    Log.d(tag, "   - success: ${signUpResponse.success}")

                    if (signUpResponse.success && signUpResponse.response != null) {
                        Log.d(tag, "✅ [회원가입] 성공!")
                        Log.d(tag, "   - 임시 사용자 ID: ${signUpResponse.response.temporaryUserId}")

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
                        Log.d(tag, "❓ [API] 성공 상태이지만 데이터 없음")
                        emit(Result.failure(Exception("알 수 없는 오류가 발생했습니다.")))
                    }
                } ?: run {
                    Log.d(tag, "❌ [API] 성공 응답 본문이 비어있음")
                    emit(Result.failure(Exception("응답 본문이 비어있습니다.")))
                }
            } else {
                // 에러 응답 처리 - errorBody() 사용
                response.errorBody()?.let { errorBody ->
                    try {
                        val errorJson = errorBody.string()
                        Log.d(tag, "📄 [API] 에러 응답 본문 파싱")
                        Log.d(tag, "   - 원본 JSON: $errorJson")

                        val gson = com.google.gson.Gson()
                        val errorResponse = gson.fromJson(
                            errorJson,
                            com.luckydut97.lighton.data.network.model.response.SignUpResponse::class.java
                        )

                        if (errorResponse?.error != null) {
                            Log.e(tag, "⚠️ [API] 서버 에러 메시지 추출 성공")
                            Log.e(tag, "   - 상태: ${errorResponse.error.status}")
                            Log.e(tag, "   - 메시지: ${errorResponse.error.message}")
                            emit(Result.failure(Exception(errorResponse.error.getUserFriendlyMessage())))
                        } else {
                            Log.e(tag, "❓ [API] 에러 응답 구조가 예상과 다름")
                            emit(Result.failure(Exception("서버 오류가 발생했습니다.")))
                        }
                    } catch (e: Exception) {
                        Log.e(tag, "💥 [API] 에러 응답 파싱 실패")
                        Log.e(tag, "   - 파싱 에러: ${e.message}")
                        emit(Result.failure(Exception("회원가입 실패: ${response.code()}")))
                    }
                } ?: run {
                    Log.e(tag, "❌ [API] 에러 응답 본문도 비어있음")
                    emit(Result.failure(Exception("회원가입 실패: ${response.code()}")))
                }
            }
        } catch (e: IOException) {
            Log.e(tag, "🌐 [네트워크] 연결 오류")
            Log.e(tag, "   - 에러: ${e.message}")
            emit(Result.failure(Exception("네트워크 연결 오류가 발생했습니다.")))
        } catch (e: Exception) {
            Log.e(tag, "💥 예상치 못한 오류 발생")
            Log.e(tag, "   - 에러: ${e.message}")
            Log.e(tag, "   - 스택 트레이스: ${e.stackTraceToString()}")
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
        val tag = "🔍 디버깅: AuthRepositoryImpl"
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
                        Log.d(tag, "📄 [API] 에러 응답 본문 파싱")
                        Log.d(tag, "   - 원본 JSON: $errorJson")

                        // JSON을 직접 파싱하여 에러 메시지 추출
                        val gson = com.google.gson.Gson()
                        val errorResponse = gson.fromJson(
                            errorJson,
                            com.luckydut97.lighton.data.network.model.response.SocialSignUpResponse::class.java
                        )

                        if (errorResponse?.error != null) {
                            Log.e(tag, "⚠️ [API] 서버 에러 메시지 추출 성공")
                            Log.e(tag, "   - 상태: ${errorResponse.error.status}")
                            Log.e(tag, "   - 메시지: ${errorResponse.error.message}")
                            emit(Result.failure(Exception(errorResponse.error.message)))
                        } else {
                            Log.e(tag, "❓ [API] 에러 응답 구조가 예상과 다름")
                            emit(Result.failure(Exception("서버 오류가 발생했습니다.")))
                        }
                    } catch (e: Exception) {
                        Log.e(tag, "💥 [API] 에러 응답 파싱 실패")
                        Log.e(tag, "   - 파싱 에러: ${e.message}")
                        emit(Result.failure(Exception("소셜 회원가입 실패: ${response.code()}")))
                    }
                } ?: run {
                    Log.e(tag, "❌ [API] 에러 응답 본문도 비어있음")
                    emit(Result.failure(Exception("소셜 회원가입 실패: ${response.code()}")))
                }
            }
        } catch (e: IOException) {
            Log.e(tag, "🌐 [네트워크] 연결 오류")
            Log.e(tag, "   - 에러: ${e.message}")
            emit(Result.failure(Exception("네트워크 연결 오류가 발생했습니다.")))
        } catch (e: Exception) {
            Log.e(tag, "💥 예상치 못한 오류 발생")
            Log.e(tag, "   - 에러: ${e.message}")
            Log.e(tag, "   - 스택 트레이스: ${e.stackTraceToString()}")
            emit(Result.failure(Exception("소셜 회원가입 중 오류가 발생했습니다: ${e.message}")))
        }
    }

    override suspend fun completePersonalInfo(
        temporaryUserId: Int,
        personalInfo: PersonalInfoData
    ): Flow<Result<User>> = flow {
        val tag = "🔍 디버깅: PersonalInfoRepo"

        try {
            Log.d(tag, "=== 개인정보 입력 완료 API 호출 시작 ===")
            Log.d(tag, "임시 사용자 ID: $temporaryUserId")
            Log.d(tag, "입력받은 개인정보:")
            Log.d(tag, "  - 이름: ${personalInfo.name}")
            Log.d(tag, "  - 전화번호: ${personalInfo.phone}")
            Log.d(tag, "  - 지역코드: ${personalInfo.regionCode}")
            Log.d(
                tag,
                "  - 약관동의: 이용약관=${personalInfo.agreements.terms}, 개인정보=${personalInfo.agreements.privacy}, 만14세=${personalInfo.agreements.over14}"
            )
            Log.d(
                tag,
                "  - 마케팅동의: SMS=${personalInfo.agreements.marketing.sms}, 푸시=${personalInfo.agreements.marketing.push}, 이메일=${personalInfo.agreements.marketing.email}"
            )
            Log.d(tag, "Endpoint: POST /api/members/$temporaryUserId/info")
            Log.d(tag, "Base URL: ${NetworkModule.getBaseUrl()}")

            val request = PersonalInfoRequest(
                name = personalInfo.name,
                phone = personalInfo.phone,
                regionCode = personalInfo.regionCode,
                agreements = AgreementRequest(
                    terms = personalInfo.agreements.terms,
                    privacy = personalInfo.agreements.privacy,
                    over14 = personalInfo.agreements.over14,
                    marketing = MarketingAgreementRequest(
                        sms = personalInfo.agreements.marketing.sms,
                        push = personalInfo.agreements.marketing.push,
                        email = personalInfo.agreements.marketing.email
                    )
                )
            )

            Log.d(tag, "Request Body 생성:")
            Log.d(tag, "  - name: ${request.name}")
            Log.d(tag, "  - phone: ${request.phone}")
            Log.d(tag, "  - regionCode: ${request.regionCode}")
            Log.d(tag, "  - agreements.terms: ${request.agreements.terms}")
            Log.d(tag, "  - agreements.privacy: ${request.agreements.privacy}")
            Log.d(tag, "  - agreements.over14: ${request.agreements.over14}")
            Log.d(tag, "  - agreements.marketing: ${request.agreements.marketing}")

            Log.d(tag, "Retrofit API 호출 시작...")
            val response = authApi.completePersonalInfo(temporaryUserId, request)

            Log.d(tag, "HTTP Status Code: ${response.code()}")
            Log.d(tag, "HTTP Status Message: ${response.message()}")
            Log.d(tag, "Response Headers: ${response.headers()}")

            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    Log.d(tag, "개인정보 입력 완료 API 호출 성공!")
                    Log.d(tag, "  - success: ${loginResponse.success}")

                    if (loginResponse.success && loginResponse.response != null) {
                        val loginData = loginResponse.response

                        Log.d(tag, "🎉 개인정보 입력 완료 성공!")
                        Log.d(tag, "🔐 토큰 정보:")
                        Log.d(tag, "  - 액세스 토큰: ${loginData.accessToken.take(50)}...")
                        Log.d(tag, "  - 리프레시 토큰: ${loginData.refreshToken.take(50)}...")

                        Log.d(tag, "⚠️ [토큰] ViewModel 또는 상위 레이어에서 토큰 저장 필요")

                        val user = User(
                            id = temporaryUserId.toString(),
                            email = "temp@example.com", // TODO: 실제 사용자 이메일 가져오기
                            name = personalInfo.name,
                            profileImageUrl = null,
                            phoneNumber = personalInfo.phone,
                            preferredGenres = emptyList(),
                            preferredRegion = personalInfo.regionCode.toString() // TODO: 지역 코드를 지역명으로 변환
                        )

                        Log.d(tag, "✅ User 객체 생성 완료:")
                        Log.d(tag, "  - ID: ${user.id}")
                        Log.d(tag, "  - 이름: ${user.name}")
                        Log.d(tag, "  - 전화번호: ${user.phoneNumber}")
                        Log.d(tag, "  - 선호지역: ${user.preferredRegion}")

                        emit(Result.success(user))
                    } else if (loginResponse.error != null) {
                        Log.e(tag, "❌ 서버에서 에러 응답 반환")
                        Log.e(tag, "  - 에러 상태: ${loginResponse.error.status}")
                        Log.e(tag, "  - 에러 메시지: ${loginResponse.error.message}")
                        emit(Result.failure(Exception(loginResponse.error.getUserFriendlyMessage())))
                    } else {
                        Log.e(tag, "❓ 성공 상태이지만 응답 데이터 없음")
                        emit(Result.failure(Exception("알 수 없는 오류가 발생했습니다.")))
                    }
                } ?: run {
                    Log.e(tag, "❌ 성공 응답 본문이 비어있음")
                    emit(Result.failure(Exception("응답 본문이 비어있습니다.")))
                }
            } else {
                // 에러 응답 처리
                val errorBody = response.errorBody()?.string()
                Log.e(tag, "❌ 개인정보 입력 완료 API 호출 실패!")
                Log.e(tag, "  - Error Code: ${response.code()}")
                Log.e(tag, "  - Error Message: ${response.message()}")
                Log.e(tag, "  - Error Body: $errorBody")

                errorBody?.let { errorJson ->
                    try {
                        Log.d(tag, "📄 에러 응답 본문 파싱 시도...")
                        val gson = com.google.gson.Gson()
                        val errorResponse = gson.fromJson(
                            errorJson,
                            com.luckydut97.lighton.data.network.model.response.CompletePersonalInfoResponse::class.java
                        )

                        if (errorResponse?.error != null) {
                            Log.w(tag, "⚠️ 서버 에러 메시지 추출 성공")
                            Log.w(tag, "  - 상태: ${errorResponse.error.status}")
                            Log.w(tag, "  - 메시지: ${errorResponse.error.message}")
                            Log.w(
                                tag,
                                "  - 사용자 친화적 메시지: ${errorResponse.error.getUserFriendlyMessage()}"
                            )
                            emit(Result.failure(Exception(errorResponse.error.getUserFriendlyMessage())))
                        } else {
                            Log.e(tag, "❓ 에러 응답 구조가 예상과 다름")
                            emit(Result.failure(Exception("서버 오류가 발생했습니다.")))
                        }
                    } catch (e: Exception) {
                        Log.e(tag, "💥 에러 응답 파싱 실패")
                        Log.e(tag, "  - 파싱 에러: ${e.message}")
                        emit(Result.failure(Exception("개인정보 입력 실패: ${response.code()}")))
                    }
                } ?: run {
                    Log.e(tag, "❌ 에러 응답 본문도 비어있음")
                    emit(Result.failure(Exception("개인정보 입력 실패: ${response.code()}")))
                }
            }
        } catch (e: IOException) {
            Log.e(tag, "🌐 네트워크 연결 오류")
            Log.e(tag, "  - 에러: ${e.message}")
            emit(Result.failure(Exception("네트워크 연결 오류가 발생했습니다.")))
        } catch (e: Exception) {
            Log.e(tag, "💥 예상치 못한 오류 발생")
            Log.e(tag, "  - 에러: ${e.message}")
            Log.e(tag, "  - 스택 트레이스: ${e.stackTraceToString()}")
            emit(Result.failure(Exception("개인정보 입력 중 오류가 발생했습니다: ${e.message}")))
        } finally {
            Log.d(tag, "=== 개인정보 입력 완료 API 호출 종료 ===")
        }
    }

    override suspend fun getCurrentUser(): Flow<User?> = flow {
        val tag = "🔍 디버깅: AuthRepositoryImpl"
        // TODO: 토큰 확인하여 현재 사용자 정보 반환
        emit(null)
    }

    override suspend fun logout(): Result<Unit> {
        val tag = "🔍 디버깅: AuthRepositoryImpl"
        return try {
            // TODO: 토큰 삭제 및 서버 로그아웃 API 호출
            Log.d(tag, "로그아웃 완료")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(tag, "💥 예상치 못한 오류 발생")
            Log.e(tag, "  - 에러: ${e.message}")
            Log.e(tag, "  - 스택 트레이스: ${e.stackTraceToString()}")
            Result.failure(e)
        }
    }

    override suspend fun checkEmailDuplicate(email: String): Flow<Result<Boolean>> = flow {
        val tag = "🔍 디버깅: AuthRepositoryImpl"
        try {
            Log.d(tag, "🚀 [API] 이메일 중복 확인 요청 전송")
            Log.d(
                tag,
                "   - URL: GET ${NetworkModule.getBaseUrl()}api/members/duplicate-check?email=$email"
            )

            val response = authApi.checkEmailDuplicate(email)

            Log.d(tag, "📡 [API] 이메일 중복 확인 응답 수신")
            Log.d(tag, "   - HTTP 상태: ${response.code()}")

            if (response.isSuccessful) {
                response.body()?.let { duplicateResponse ->
                    Log.d(tag, "📄 [API] 성공 응답 본문 파싱")
                    Log.d(tag, "   - success: ${duplicateResponse.success}")

                    if (duplicateResponse.success && duplicateResponse.response != null) {
                        val isDuplicated = duplicateResponse.response.isDuplicated
                        Log.d(tag, "✅ [중복 확인] 성공!")
                        Log.d(tag, "   - 중복 여부: $isDuplicated")
                        emit(Result.success(isDuplicated))
                    } else {
                        Log.d(tag, "❓ [API] 성공 상태이지만 데이터 없음")
                        emit(Result.failure(Exception("중복 확인 중 오류가 발생했습니다.")))
                    }
                } ?: run {
                    Log.d(tag, "❌ [API] 성공 응답 본문이 비어있음")
                    emit(Result.failure(Exception("응답 본문이 비어있습니다.")))
                }
            } else {
                // 에러 응답 처리 - errorBody() 사용
                response.errorBody()?.let { errorBody ->
                    try {
                        val errorJson = errorBody.string()
                        Log.d(tag, "📄 [API] 에러 응답 본문 파싱")
                        Log.d(tag, "   - 원본 JSON: $errorJson")

                        val gson = com.google.gson.Gson()
                        val errorResponse = gson.fromJson(
                            errorJson,
                            com.luckydut97.lighton.data.network.model.response.EmailDuplicateCheckResponse::class.java
                        )

                        if (errorResponse?.error != null) {
                            Log.e(tag, "⚠️ [API] 서버 에러 메시지 추출 성공")
                            Log.e(tag, "   - 상태: ${errorResponse.error.status}")
                            Log.e(tag, "   - 메시지: ${errorResponse.error.message}")
                            emit(Result.failure(Exception(errorResponse.error.getUserFriendlyMessage())))
                        } else {
                            Log.e(tag, "❓ [API] 에러 응답 구조가 예상과 다름")
                            emit(Result.failure(Exception("서버 오류가 발생했습니다.")))
                        }
                    } catch (e: Exception) {
                        Log.e(tag, "💥 [API] 에러 응답 파싱 실패")
                        Log.e(tag, "   - 파싱 에러: ${e.message}")
                        emit(Result.failure(Exception("중복 확인 실패: ${response.code()}")))
                    }
                } ?: run {
                    Log.e(tag, "❌ [API] 에러 응답 본문도 비어있음")
                    emit(Result.failure(Exception("중복 확인 실패: ${response.code()}")))
                }
            }
        } catch (e: IOException) {
            Log.e(tag, "🌐 [네트워크] 연결 오류")
            Log.e(tag, "   - 에러: ${e.message}")
            emit(Result.failure(Exception("네트워크 연결 오류가 발생했습니다.")))
        } catch (e: Exception) {
            Log.e(tag, "💥 예상치 못한 오류 발생")
            Log.e(tag, "   - 에러: ${e.message}")
            Log.e(tag, "   - 스택 트레이스: ${e.stackTraceToString()}")
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
