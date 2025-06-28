package com.luckydut97.lighton.feature_auth.signup.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luckydut97.domain.usecase.PersonalInfoData
import com.luckydut97.lighton.core.ui.components.CommonTopBar
import com.luckydut97.lighton.core.ui.components.LightonAgreementCheckbox
import com.luckydut97.lighton.core.ui.components.LightonCheckbox
import com.luckydut97.lighton.core.ui.components.LightonDropdown
import com.luckydut97.lighton.core.ui.components.LightonInputField
import com.luckydut97.lighton.core.ui.components.LightonNextButton
import com.luckydut97.lighton.core.ui.components.PhoneVerificationField
import com.luckydut97.lighton.core.ui.components.ValidationResult
import com.luckydut97.lighton.core.ui.theme.CaptionColor
import com.luckydut97.lighton.core.ui.theme.LightonTheme
import com.luckydut97.lighton.core.ui.theme.PretendardFamily
import com.luckydut97.lighton.data.repository.PhoneVerificationRepository
import com.luckydut97.lighton.feature_auth.signup.viewmodel.PersonalInfoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PersonalInfoScreen(
    temporaryUserId: Int? = null,
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onCompleteClick: () -> Unit = {},
    viewModel: PersonalInfoViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var verificationCode by remember { mutableStateOf("") }
    var isPhoneVerified by remember { mutableStateOf(false) }
    var phoneValidationResult by remember { mutableStateOf<ValidationResult>(ValidationResult.Initial) }
    var showVerificationCodeField by remember { mutableStateOf(false) }
    var timerSeconds by remember { mutableIntStateOf(180) }
    var resendButtonDelay by remember { mutableIntStateOf(10) }

    var selectedRegion by remember { mutableStateOf("") }
    var selectedDistrict by remember { mutableStateOf("") }

    var agreeAll by remember { mutableStateOf(false) }
    var agreeEntrance by remember { mutableStateOf(false) }
    var agreeSMS by remember { mutableStateOf(false) }
    var agreeAppPush by remember { mutableStateOf(false) }
    var agreeEmail by remember { mutableStateOf(false) }
    var agreeTerms by remember { mutableStateOf(false) }
    var agreePrivacyTerms by remember { mutableStateOf(false) }
    var agreeAgeTerms by remember { mutableStateOf(false) }

    val regionCodeMap = mapOf(
        "서울시" to 212,
        "부산광역시" to 213,
        "인천광역시" to 214,
        "대구광역시" to 215,
        "광주광역시" to 216,
        "대전광역시" to 217,
        "울산광역시" to 218
    )

    val cities = regionCodeMap.keys.toList()
    val districts = listOf("중구", "서구", "동구", "남구", "북구", "영도구", "부산진구")

    fun updateAllAgreements(checked: Boolean) {
        agreeAll = checked
        agreeEntrance = checked
        agreeSMS = checked
        agreeAppPush = checked
        agreeEmail = checked
        agreeTerms = checked
        agreePrivacyTerms = checked
        agreeAgeTerms = checked
    }

    val coroutineScope = rememberCoroutineScope()

    fun startTimer() {
        timerSeconds = 180
        resendButtonDelay = 10
        coroutineScope.launch {
            while (timerSeconds > 0) {
                delay(1000)
                timerSeconds -= 1
            }
        }
        coroutineScope.launch {
            while (resendButtonDelay > 0) {
                delay(1000)
                resendButtonDelay -= 1
            }
        }
    }

    val verifyPhone: () -> Unit = {
        if (phoneNumber.isNotEmpty()) {
            showVerificationCodeField = true
            startTimer()
            coroutineScope.launch {
                val phoneRepository = PhoneVerificationRepository()
                val result = phoneRepository.requestVerificationCode(phoneNumber)
                if (result.success) {
                    phoneValidationResult = ValidationResult.Initial
                } else {
                    phoneValidationResult = ValidationResult.Invalid(
                        result.error?.message ?: "인증번호 발송에 실패했습니다."
                    )
                    showVerificationCodeField = false
                }
            }
        } else {
            phoneValidationResult = ValidationResult.Invalid("휴대폰 번호를 입력해주세요.")
        }
    }

    val verifyCode: () -> Unit = {
        if (verificationCode.isNotEmpty()) {
            coroutineScope.launch {
                val phoneRepository = PhoneVerificationRepository()
                val result = phoneRepository.verifyPhoneCode(phoneNumber, verificationCode)
                if (result.success) {
                    isPhoneVerified = true
                    phoneValidationResult = ValidationResult.Valid
                    showVerificationCodeField = false
                } else {
                    phoneValidationResult = ValidationResult.Invalid(
                        result.error?.message ?: "인증번호가 올바르지 않습니다."
                    )
                }
            }
        } else {
            phoneValidationResult = ValidationResult.Invalid("")
        }
    }

    val resendCode: () -> Unit = {
        phoneValidationResult = ValidationResult.Initial
        verificationCode = ""
        startTimer()
        coroutineScope.launch {
            val phoneRepository = PhoneVerificationRepository()
            val result = phoneRepository.requestVerificationCode(phoneNumber)
            if (!result.success) {
                phoneValidationResult = ValidationResult.Invalid(
                    result.error?.message ?: "재전송에 실패했습니다."
                )
            }
        }
    }

    val isFormValid = name.isNotEmpty() &&
            isPhoneVerified &&
            selectedRegion.isNotEmpty() &&
            selectedDistrict.isNotEmpty() &&
            agreeTerms &&
            agreePrivacyTerms &&
            agreeAgeTerms

    fun completePersonalInfo() {
        val regionCode = regionCodeMap[selectedRegion] ?: return

        val personalInfo = PersonalInfoData(
            name = name,
            phone = phoneNumber,
            regionCode = regionCode,
            agreeTerms = agreeTerms,
            agreePrivacy = agreePrivacyTerms,
            agreeOver14 = agreeAgeTerms,
            agreeSMS = agreeSMS,
            agreePush = agreeAppPush,
            agreeEmail = agreeEmail
        )

        temporaryUserId?.let { userId ->
            viewModel.completePersonalInfo(userId, personalInfo)
        }
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onCompleteClick()
        }
    }

    DisposableEffect(Unit) {
        onDispose {}
    }

    LightonTheme {
        Scaffold { paddingValues ->
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    CommonTopBar(
                        title = if (temporaryUserId != null) "개인정보 입력" else "회원가입",
                        onBackClick = onBackClick
                    )

                    uiState.errorMessage?.let { errorMessage ->
                        Text(
                            text = errorMessage,
                            color = Color.Red,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 17.dp, vertical = 8.dp)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 17.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = "개인정보 입력",
                            fontFamily = PretendardFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(top = 24.dp, bottom = 20.dp)
                        )

                        LightonInputField(
                            label = "이름",
                            value = name,
                            onValueChange = { name = it },
                            isRequired = true,
                            placeholder = "이름을 입력해주세요.",
                            keyboardType = KeyboardType.Text
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        PhoneVerificationField(
                            phone = phoneNumber,
                            onPhoneChange = { phoneNumber = it },
                            verificationCode = verificationCode,
                            onVerificationCodeChange = { verificationCode = it },
                            onSendClick = verifyPhone,
                            onVerifyClick = verifyCode,
                            onResendClick = resendCode,
                            codeTimerSec = timerSeconds,
                            resendEnabled = resendButtonDelay <= 0,
                            resendCooldownSec = resendButtonDelay,
                            phoneFieldEnabled = !isPhoneVerified,
                            showCodeField = showVerificationCodeField,
                            isVerified = isPhoneVerified,
                            errorMsg = if (phoneValidationResult is ValidationResult.Invalid) (phoneValidationResult as ValidationResult.Invalid).message else null
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier
                                .height(18.dp)
                                .padding(start = 16.dp)
                        ) {
                            Text(
                                text = "선호 지역",
                                color = CaptionColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = PretendardFamily
                            )

                            Text(
                                text = " *",
                                color = Color.Red,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = PretendardFamily
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            LightonDropdown(
                                label = "",
                                selectedItem = selectedRegion,
                                items = cities,
                                onItemSelected = { selectedRegion = it },
                                placeholder = "도/시/군",
                                isRequired = false,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 4.dp)
                            )

                            LightonDropdown(
                                label = "",
                                selectedItem = selectedDistrict,
                                items = districts,
                                onItemSelected = { selectedDistrict = it },
                                placeholder = "읍/면/동",
                                isRequired = false,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Text(
                            text = "마케팅 정보 수신 (선택)",
                            fontFamily = PretendardFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        LightonCheckbox(
                            text = "출입 방법 저장",
                            checked = agreeEntrance,
                            onCheckedChange = { agreeEntrance = it }
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            LightonCheckbox(
                                text = "SMS",
                                checked = agreeSMS,
                                onCheckedChange = { agreeSMS = it },
                                modifier = Modifier.weight(1f)
                            )

                            LightonCheckbox(
                                text = "앱 푸시",
                                checked = agreeAppPush,
                                onCheckedChange = { agreeAppPush = it },
                                modifier = Modifier.weight(1f)
                            )

                            LightonCheckbox(
                                text = "이메일",
                                checked = agreeEmail,
                                onCheckedChange = { agreeEmail = it },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Text(
                            text = "* 수신 동의 상태는 개인 설정에서 별도로 변경할 수 있습니다.",
                            fontFamily = PretendardFamily,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "약관 동의",
                            fontFamily = PretendardFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        LightonAgreementCheckbox(
                            title = "이용약관에 동의합니다. (필수)",
                            checked = agreeTerms,
                            onCheckedChange = { agreeTerms = it },
                            showDetailButton = true,
                            onDetailClick = { /* 약관 내용 보기 */ }
                        )

                        LightonAgreementCheckbox(
                            title = "개인정보 수집 및 이용에 동의합니다. (필수)",
                            checked = agreePrivacyTerms,
                            onCheckedChange = { agreePrivacyTerms = it },
                            showDetailButton = true,
                            onDetailClick = { /* 약관 내용 보기 */ }
                        )

                        LightonAgreementCheckbox(
                            title = "만 14세 이상입니다. (필수)",
                            checked = agreeAgeTerms,
                            onCheckedChange = { agreeAgeTerms = it },
                            showDetailButton = false
                        )

                        Spacer(modifier = Modifier.height(80.dp))
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 17.dp, vertical = 16.dp)
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        } else {
                            LightonNextButton(
                                text = if (temporaryUserId != null) "완료" else "다음",
                                isEnabled = isFormValid,
                                onClick = {
                                    if (temporaryUserId != null) {
                                        completePersonalInfo()
                                    } else {
                                        onNextClick()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalInfoScreenPreview() {
    LightonTheme {
        PersonalInfoScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalInfoScreenSocialPreview() {
    LightonTheme {
        PersonalInfoScreen(temporaryUserId = 123)
    }
}
