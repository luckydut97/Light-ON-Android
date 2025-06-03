package com.luckydut97.lighton.feature_auth.signup.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.luckydut97.lighton.core.ui.components.LightonBackButton
import com.luckydut97.lighton.core.ui.components.LightonInputField
import com.luckydut97.lighton.core.ui.components.LightonNextButton
import com.luckydut97.lighton.core.ui.components.ValidationResult
import com.luckydut97.lighton.core.ui.theme.LightonTheme
import com.luckydut97.lighton.core.ui.theme.PretendardFamily

@Composable
fun SignUpScreen(
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {}
) {
    // 입력값 상태/결과 상태 선언
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isEmailChecked by remember { mutableStateOf(false) }
    var emailValidationResult by remember { mutableStateOf<ValidationResult>(ValidationResult.Initial) }
    var passwordValidationResult by remember { mutableStateOf<ValidationResult>(ValidationResult.Initial) }
    var confirmPasswordValidationResult by remember { mutableStateOf<ValidationResult>(ValidationResult.Initial) }

    // 이메일 중복 확인 함수 (동일)
    val checkEmailDuplicate: () -> Unit = {
        if (email == "duplicate@example.com") {
            emailValidationResult = ValidationResult.Invalid("중복된 이메일 주소입니다.")
            isEmailChecked = false
        }
        else if (!email.contains('@') || !email.lowercase().contains(".com")) {
            emailValidationResult = ValidationResult.Invalid("올바른 메일주소를 입력하세요.")
            isEmailChecked = false
        } else {
            emailValidationResult = ValidationResult.Valid
            isEmailChecked = true
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

    // 전체 제출 가능 체크 (동일)
    val isFormValid = isEmailChecked &&
            emailValidationResult is ValidationResult.Valid &&
            passwordValidationResult is ValidationResult.Valid &&
            confirmPasswordValidationResult is ValidationResult.Valid

    LightonTheme {
        Scaffold(
            bottomBar = {
                // 하단 버튼을 고정으로 배치
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    LightonNextButton(
                        text = "다음",
                        isEnabled = isFormValid,
                        onClick = {
                            // 다음 화면(PersonalInfoScreen)으로 이동
                            onNextClick() // 네비게이션 핸들러를 통해 PersonalInfoScreen으로 이동
                        }
                    )
                }
            }
        ) { paddingValues ->
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
                    // 상단 툴바 - 여백 추가
                    Spacer(modifier = Modifier.height(16.dp))

                    // 뒤로가기 버튼과 제목
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    ) {
                        LightonBackButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 17.dp)
                        )

                        Text(
                            text = "회원가입",
                            fontFamily = PretendardFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    // 메인 콘텐츠를 스크롤 가능하게
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
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

                        // 하단 여백
                        Spacer(modifier = Modifier.height(40.dp))
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
