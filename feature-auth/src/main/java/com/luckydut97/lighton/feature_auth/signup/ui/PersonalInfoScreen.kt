package com.luckydut97.lighton.feature_auth.signup.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.luckydut97.lighton.core.ui.components.LightonAgreementCheckbox
import com.luckydut97.lighton.core.ui.components.LightonBackButton
import com.luckydut97.lighton.core.ui.components.LightonCheckbox
import com.luckydut97.lighton.core.ui.components.LightonDropdown
import com.luckydut97.lighton.core.ui.components.LightonInputField
import com.luckydut97.lighton.core.ui.components.LightonNextButton
import com.luckydut97.lighton.core.ui.components.ValidationResult
import com.luckydut97.lighton.core.ui.theme.LightonTheme
import com.luckydut97.lighton.core.ui.theme.PretendardFamily

@Composable
fun PersonalInfoScreen(
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {}
) {
    // 상태 관리
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var isPhoneVerified by remember { mutableStateOf(false) }
    var phoneValidationResult by remember { mutableStateOf<ValidationResult>(ValidationResult.Initial) }

    // 지역 선택 상태
    var selectedRegion by remember { mutableStateOf("") }
    var selectedDistrict by remember { mutableStateOf("") }

    // 전체 동의 상태
    var agreeAll by remember { mutableStateOf(false) }

    // 마케팅 수신 동의 상태
    var agreeEntrance by remember { mutableStateOf(false) }
    var agreeSMS by remember { mutableStateOf(false) }
    var agreeAppPush by remember { mutableStateOf(false) }
    var agreeEmail by remember { mutableStateOf(false) }

    // 약관 동의 상태
    var agreeTerms by remember { mutableStateOf(false) }
    var agreePrivacyTerms by remember { mutableStateOf(false) }
    var agreeAgeTerms by remember { mutableStateOf(false) }

    // 모든 필수 입력이 유효한지 확인
    val isFormValid = name.isNotEmpty() &&
            isPhoneVerified &&
            selectedRegion.isNotEmpty() &&
            selectedDistrict.isNotEmpty() &&
            agreeTerms &&
            agreePrivacyTerms &&
            agreeAgeTerms

    // 인증받기 함수
    val verifyPhone: () -> Unit = {
        // 실제로는 SMS 인증 등 구현
        isPhoneVerified = true
        phoneValidationResult = ValidationResult.Valid
    }

    // 도시 목록 샘플
    val cities = listOf("서울시", "부산광역시", "인천광역시", "대구광역시", "광주광역시", "대전광역시", "울산광역시")

    // 읍면동 목록 샘플
    val districts = listOf("중구", "서구", "동구", "남구", "북구", "영도구", "부산진구")

    // 전체 동의 기능
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
                            .weight(1f) // 스크롤 영역을 지정하기 위해 weight 사용
                            .padding(horizontal = 17.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // 개인정보 입력 타이틀
                        Text(
                            text = "개인정보 입력",
                            fontFamily = PretendardFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(top = 24.dp, bottom = 20.dp)
                        )

                        // 이름 입력 필드 - LightonInputField 컴포넌트 사용
                        LightonInputField(
                            label = "이름",
                            value = name,
                            onValueChange = { name = it },
                            isRequired = true,
                            placeholder = "이름을 입력해주세요.",
                            keyboardType = KeyboardType.Text
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // 휴대폰 번호 입력 필드 - LightonInputField 컴포넌트 사용
                        LightonInputField(
                            label = "휴대폰 번호",
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            isRequired = true,
                            placeholder = "휴대폰 번호를 입력해주세요.",
                            keyboardType = KeyboardType.Phone,
                            validationResult = phoneValidationResult,
                            enableVerifyButton = !isPhoneVerified,
                            onVerifyClick = verifyPhone
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // 선호 지역 선택
                        Text(
                            text = "선호 지역 *",
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = PretendardFamily,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // 도/시/군 선택
                            LightonDropdown(
                                label = "", // 빈 문자열로 설정하여 라벨 영역을 표시하지 않음
                                selectedItem = selectedRegion,
                                items = cities,
                                onItemSelected = { selectedRegion = it },
                                placeholder = "도/시/군",
                                isRequired = false,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 4.dp)
                            )

                            // 읍/면/동 선택
                            LightonDropdown(
                                label = "", // 빈 문자열로 설정하여 라벨 영역을 표시하지 않음
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

                        // 마케팅 정보 수신 섹션
                        Text(
                            text = "마케팅 정보 수신 (선택)",
                            fontFamily = PretendardFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // 출입 방법 저장 체크박스 - LightonCheckbox 컴포넌트 사용
                        LightonCheckbox(
                            text = "출입 방법 저장",
                            checked = agreeEntrance,
                            onCheckedChange = { agreeEntrance = it }
                        )

                        // SMS, 앱 푸시, 이메일 체크박스 (가로 정렬)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            // SMS 체크박스
                            LightonCheckbox(
                                text = "SMS",
                                checked = agreeSMS,
                                onCheckedChange = { agreeSMS = it },
                                modifier = Modifier.weight(1f)
                            )

                            // 앱 푸시 체크박스
                            LightonCheckbox(
                                text = "앱 푸시",
                                checked = agreeAppPush,
                                onCheckedChange = { agreeAppPush = it },
                                modifier = Modifier.weight(1f)
                            )

                            // 이메일 체크박스
                            LightonCheckbox(
                                text = "이메일",
                                checked = agreeEmail,
                                onCheckedChange = { agreeEmail = it },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // 회색 설명 텍스트
                        Text(
                            text = "* 수신 동의 상태는 개인 설정에서 별도로 변경할 수 있습니다.",
                            fontFamily = PretendardFamily,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // 약관 동의 섹션
                        Text(
                            text = "약관 동의",
                            fontFamily = PretendardFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // 전체 동의 체크박스
                        LightonCheckbox(
                            text = "전체 동의합니다.",
                            checked = agreeAll,
                            onCheckedChange = { updateAllAgreements(it) }
                        )

                        // 이용약관 동의 체크박스 - LightonAgreementCheckbox 컴포넌트 사용
                        LightonAgreementCheckbox(
                            title = "이용약관에 동의합니다. (필수)",
                            checked = agreeTerms,
                            onCheckedChange = { agreeTerms = it },
                            showDetailButton = true,
                            onDetailClick = { /* 약관 내용 보기 */ }
                        )

                        // 개인정보 수집 및 이용 동의 체크박스
                        LightonAgreementCheckbox(
                            title = "개인정보 수집 및 이용에 동의합니다. (필수)",
                            checked = agreePrivacyTerms,
                            onCheckedChange = { agreePrivacyTerms = it },
                            showDetailButton = true,
                            onDetailClick = { /* 약관 내용 보기 */ }
                        )

                        // 14세 이상 동의 체크박스
                        LightonAgreementCheckbox(
                            title = "만 14세 이상입니다. (필수)",
                            checked = agreeAgeTerms,
                            onCheckedChange = { agreeAgeTerms = it },
                            showDetailButton = false
                        )

                        // 하단 여백
                        Spacer(modifier = Modifier.height(80.dp))
                    }

                    // 하단 버튼 영역 - LightonNextButton 컴포넌트 사용
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 17.dp, vertical = 16.dp)
                    ) {
                        LightonNextButton(
                            text = "다음",
                            isEnabled = isFormValid,
                            onClick = onNextClick
                        )
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