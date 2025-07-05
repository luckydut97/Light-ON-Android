package com.luckydut97.lighton.feature_auth.signup.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.luckydut97.lighton.core.ui.components.LightonBackButton
import com.luckydut97.lighton.core.ui.components.LightonInputField
import com.luckydut97.lighton.core.ui.components.LightonNextButton
import com.luckydut97.lighton.core.ui.components.ValidationResult
import com.luckydut97.lighton.core.ui.components.CommonTopBar
import com.luckydut97.lighton.core.ui.theme.LightonTheme
import com.luckydut97.lighton.core.ui.theme.PretendardFamily
import com.luckydut97.lighton.feature_auth.signup.viewmodel.SignupViewModel
import com.luckydut97.lighton.feature_auth.signup.viewmodel.EmailCheckResult
import com.luckydut97.lighton.feature_auth.signup.viewmodel.EmailCheckResult.Error

@Composable
fun SignUpScreen(
    onBackClick: () -> Unit = {},
    onNextClick: (String) -> Unit = {},
    viewModel: SignupViewModel = viewModel()
) {
    // 입력값 상태/결과 상태 선언
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isEmailChecked by remember { mutableStateOf(false) }
    var emailValidationResult by remember { mutableStateOf<ValidationResult>(ValidationResult.Initial) }
    var passwordValidationResult by remember { mutableStateOf<ValidationResult>(ValidationResult.Initial) }
    var confirmPasswordValidationResult by remember { mutableStateOf<ValidationResult>(ValidationResult.Initial) }

    // ViewModel 상태 관찰
    val uiState by viewModel.uiState.collectAsState()

    // 이메일 중복 확인 결과에 따른 validation 업데이트
    LaunchedEffect(uiState.emailCheckResult) {
        val checkResult = uiState.emailCheckResult
        when (checkResult) {
            is com.luckydut97.lighton.feature_auth.signup.viewmodel.EmailCheckResult.Available -> {
                emailValidationResult = ValidationResult.Valid
                isEmailChecked = true
            }

            is com.luckydut97.lighton.feature_auth.signup.viewmodel.EmailCheckResult.Duplicated -> {
                emailValidationResult = ValidationResult.Invalid("이미 사용 중인 이메일입니다.")
                isEmailChecked = false
            }

            is com.luckydut97.lighton.feature_auth.signup.viewmodel.EmailCheckResult.Error -> {
                val errorMessage =
                    (checkResult as com.luckydut97.lighton.feature_auth.signup.viewmodel.EmailCheckResult.Error).message
                emailValidationResult = ValidationResult.Invalid(errorMessage)
                isEmailChecked = false
            }

            null -> {
                // 초기 상태 또는 리셋 상태
            }
        }
    }

    // 회원가입 성공시 다음 화면으로 이동
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            uiState.temporaryUserId?.let { userId ->
                onNextClick(userId)
            }
        }
    }

    // 에러 메시지 표시 (Toast나 SnackBar 대신 간단히 로그로)
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            println("🔴 회원가입 오류: $error")
        }
    }

    // 이메일 유효성 검사 함수
    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isEmpty() -> ValidationResult.Initial
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                ValidationResult.Invalid("올바른 이메일 형식을 입력해주세요.")

            else -> ValidationResult.Valid
        }
    }

    // 이메일 중복 확인 함수
    val checkEmailDuplicate: () -> Unit = {
        val emailValidation = validateEmail(email)
        if (emailValidation is ValidationResult.Valid) {
            // 이메일 형식이 올바를 때만 중복 확인 API 호출
            viewModel.checkEmailDuplicate(email)
        } else {
            // 이메일 형식이 잘못된 경우
            emailValidationResult = emailValidation
            isEmailChecked = false
        }
    }

    // 비밀번호 유효성 검사 함수 (영/숫자/특수/대소문자 및 8~20자리)
    fun validatePasswordStrict(pwd: String): ValidationResult {
        if (pwd.isEmpty()) return ValidationResult.Initial
        if (pwd.length < 8 || pwd.length > 20) {
            return ValidationResult.Invalid("비밀번호는 영문, 숫자, 특수문자 포함 8-20자리여야 합니다.")
        }
        // 소문자/대문자/숫자/특수문자 체크
        if (!pwd.any { it.isLowerCase() })
            return ValidationResult.Invalid("영문 소문자를 반드시 포함해야 합니다.")
        if (!pwd.any { it.isUpperCase() })
            return ValidationResult.Invalid("영문 대문자를 반드시 포함해야 합니다.")
        if (!pwd.any { it.isDigit() })
            return ValidationResult.Invalid("숫자를 반드시 포함해야 합니다.")
        if (!pwd.any { !it.isLetterOrDigit() })
            return ValidationResult.Invalid("특수문자를 반드시 포함해야 합니다.")
        return ValidationResult.Valid
    }

    // 비밀번호 확인 검사 (패스워드가 바뀌거나, 확인란이 바뀔 때 언제든 즉시 호출)
    fun validateConfirmPasswordValue(password: String, confirm: String): ValidationResult {
        if (confirm.isEmpty()) return ValidationResult.Initial
        else if (password != confirm) return ValidationResult.Invalid("비밀번호가 일치하지 않습니다.")
        else return ValidationResult.Valid
    }

    // ★ 입력이 바뀔때마다 항상 검증 결과도 동기화 (패스워드/확인필드 순서와 무관)
    fun onPasswordChange(newPwd: String) {
        password = newPwd
        passwordValidationResult = validatePasswordStrict(newPwd)
        // 비번이 바뀌면 비번확인도 즉시 재검증
        confirmPasswordValidationResult = validateConfirmPasswordValue(newPwd, confirmPassword)
    }

    fun onConfirmPasswordChange(newConfirm: String) {
        confirmPassword = newConfirm
        confirmPasswordValidationResult = validateConfirmPasswordValue(password, newConfirm)
    }

    // 전체 제출 가능 체크
    val isFormValid = isEmailChecked &&
            emailValidationResult is ValidationResult.Valid &&
            passwordValidationResult is ValidationResult.Valid &&
            confirmPasswordValidationResult is ValidationResult.Valid

    LightonTheme {
        Scaffold { paddingValues ->
            // 메인 콘텐츠
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // CommonTopBar 추가
                    CommonTopBar(
                        title = "회원가입",
                        onBackClick = onBackClick,

                    )

                    // 메인 콘텐츠를 스크롤 가능하게
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f) // 스크롤 영역을 지정하기 위해 weight 사용
                            .padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // 상단 여백
                        Spacer(modifier = Modifier.height(24.dp))

                        // 아이디 입력 필드
                        LightonInputField(
                            label = "아이디",
                            value = email,
                            onValueChange = {
                                email = it
                                isEmailChecked = false
                                emailValidationResult = ValidationResult.Initial
                                viewModel.resetEmailCheck() // 중복 확인 상태 초기화
                            },
                            isRequired = true,
                            placeholder = "이메일 주소를 입력해주세요",
                            keyboardType = KeyboardType.Email,
                            validationResult = emailValidationResult,
                            enableVerifyButton = true,
                            onVerifyClick = checkEmailDuplicate
                        )

                        // 아이디 입력 필드와 비밀번호 필드 사이 간격
                        Spacer(modifier = Modifier.height(24.dp))

                        // 비밀번호 입력 필드
                        LightonInputField(
                            label = "비밀번호",
                            value = password,
                            // 최대 20자까지만 허용 (초과 시 입력 안 됨)
                            onValueChange = { if (it.length <= 20) onPasswordChange(it) },
                            isRequired = true,
                            placeholder = "영문 대소문자, 숫자, 특수문자 포함 8-20자리",
                            keyboardType = KeyboardType.Password,
                            isPassword = true,
                            validationResult = passwordValidationResult
                        )

                        // 비밀번호와 비밀번호 확인 필드 사이 간격
                        Spacer(modifier = Modifier.height(24.dp))

                        // 비밀번호 확인 입력 필드
                        LightonInputField(
                            label = "비밀번호 확인",
                            value = confirmPassword,
                            // 최대 20자까지만 허용 (초과 시 입력 안 됨)
                            onValueChange = { if (it.length <= 20) onConfirmPasswordChange(it) },
                            isRequired = true,
                            placeholder = "비밀번호를 다시 입력하세요",
                            keyboardType = KeyboardType.Password,
                            isPassword = true,
                            validationResult = confirmPasswordValidationResult
                        )

                        // 에러 메시지 표시
                        uiState.errorMessage?.let { error ->
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = error,
                                color = Color.Red,
                                fontSize = 14.sp,
                                fontFamily = PretendardFamily,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }

                        // 하단 여백
                        Spacer(modifier = Modifier.height(80.dp))
                    }

                    // 하단 버튼 영역
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        LightonNextButton(
                            text = "다음",
                            isEnabled = isFormValid,
                            onClick = {
                                println("🚀 회원가입 API 호출 - 이메일: $email")
                                viewModel.signUp(email, password)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    LightonTheme {
        SignUpScreen()
    }
}
