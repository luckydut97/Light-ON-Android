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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.domain.usecase.PersonalInfoData
import com.luckydut97.domain.usecase.AgreementsData
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewmodel.compose.viewModel
import android.util.Log
import com.luckydut97.domain.usecase.MarketingAgreementsData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PersonalInfoScreen(
    temporaryUserId: String? = null,
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

    // 전체 지역 데이터 - 코드: (시/도, 구/군) 매핑
    val regionData = mapOf(
        101 to ("서울특별시" to "종로구"),
        102 to ("서울특별시" to "중구"),
        103 to ("서울특별시" to "용산구"),
        104 to ("서울특별시" to "성동구"),
        105 to ("서울특별시" to "광진구"),
        106 to ("서울특별시" to "동대문구"),
        107 to ("서울특별시" to "중랑구"),
        108 to ("서울특별시" to "성북구"),
        109 to ("서울특별시" to "강북구"),
        110 to ("서울특별시" to "도봉구"),
        111 to ("서울특별시" to "노원구"),
        112 to ("서울특별시" to "은평구"),
        113 to ("서울특별시" to "서대문구"),
        114 to ("서울특별시" to "마포구"),
        115 to ("서울특별시" to "양천구"),
        116 to ("서울특별시" to "강서구"),
        117 to ("서울특별시" to "구로구"),
        118 to ("서울특별시" to "금천구"),
        119 to ("서울특별시" to "영등포구"),
        120 to ("서울특별시" to "동작구"),
        121 to ("서울특별시" to "관악구"),
        122 to ("서울특별시" to "서초구"),
        123 to ("서울특별시" to "강남구"),
        124 to ("서울특별시" to "송파구"),
        125 to ("서울특별시" to "강동구"),
        201 to ("부산광역시" to "중구"),
        202 to ("부산광역시" to "서구"),
        203 to ("부산광역시" to "동구"),
        204 to ("부산광역시" to "영도구"),
        205 to ("부산광역시" to "부산진구"),
        206 to ("부산광역시" to "동래구"),
        207 to ("부산광역시" to "남구"),
        208 to ("부산광역시" to "북구"),
        209 to ("부산광역시" to "해운대구"),
        210 to ("부산광역시" to "사하구"),
        211 to ("부산광역시" to "금정구"),
        212 to ("부산광역시" to "연제구"),
        213 to ("부산광역시" to "수영구"),
        214 to ("부산광역시" to "사상구"),
        215 to ("부산광역시" to "기장군"),
        301 to ("대구광역시" to "중구"),
        302 to ("대구광역시" to "동구"),
        303 to ("대구광역시" to "서구"),
        304 to ("대구광역시" to "남구"),
        305 to ("대구광역시" to "북구"),
        306 to ("대구광역시" to "수성구"),
        307 to ("대구광역시" to "달서구"),
        308 to ("대구광역시" to "달성군"),
        401 to ("인천광역시" to "중구"),
        402 to ("인천광역시" to "동구"),
        403 to ("인천광역시" to "서구"),
        404 to ("인천광역시" to "미추홀구"),
        405 to ("인천광역시" to "연수구"),
        406 to ("인천광역시" to "남동구"),
        407 to ("인천광역시" to "부평구"),
        408 to ("인천광역시" to "계양구"),
        410 to ("인천광역시" to "강화군"),
        411 to ("인천광역시" to "옹진군"),
        501 to ("광주광역시" to "동구"),
        502 to ("광주광역시" to "서구"),
        503 to ("광주광역시" to "남구"),
        504 to ("광주광역시" to "북구"),
        505 to ("광주광역시" to "광산구"),
        601 to ("대전광역시" to "동구"),
        602 to ("대전광역시" to "중구"),
        603 to ("대전광역시" to "서구"),
        604 to ("대전광역시" to "유성구"),
        605 to ("대전광역시" to "대덕구"),
        701 to ("울산광역시" to "중구"),
        702 to ("울산광역시" to "남구"),
        703 to ("울산광역시" to "동구"),
        704 to ("울산광역시" to "북구"),
        705 to ("울산광역시" to "울주군"),
        801 to ("세종특별자치시" to "세종시"),
        901 to ("경기도" to "수원시"),
        902 to ("경기도" to "성남시"),
        903 to ("경기도" to "고양시"),
        904 to ("경기도" to "용인시"),
        905 to ("경기도" to "부천시"),
        906 to ("경기도" to "안산시"),
        907 to ("경기도" to "안양시"),
        908 to ("경기도" to "남양주시"),
        909 to ("경기도" to "화성시"),
        910 to ("경기도" to "평택시"),
        911 to ("경기도" to "의정부시"),
        912 to ("경기도" to "시흥시"),
        913 to ("경기도" to "파주시"),
        914 to ("경기도" to "김포시"),
        915 to ("경기도" to "광명시"),
        916 to ("경기도" to "군포시"),
        917 to ("경기도" to "이천시"),
        918 to ("경기도" to "오산시"),
        919 to ("경기도" to "하남시"),
        920 to ("경기도" to "의왕시"),
        921 to ("경기도" to "양주시"),
        922 to ("경기도" to "구리시"),
        923 to ("경기도" to "안성시"),
        924 to ("경기도" to "포천시"),
        925 to ("경기도" to "광주시"),
        926 to ("경기도" to "동두천시"),
        927 to ("경기도" to "양평군"),
        928 to ("경기도" to "여주시"),
        929 to ("경기도" to "가평군"),
        930 to ("경기도" to "연천군"),
        1001 to ("강원특별자치도" to "춘천시"),
        1002 to ("강원특별자치도" to "원주시"),
        1003 to ("강원특별자치도" to "강릉시"),
        1004 to ("강원특별자치도" to "동해시"),
        1005 to ("강원특별자치도" to "태백시"),
        1006 to ("강원특별자치도" to "속초시"),
        1007 to ("강원특별자치도" to "삼척시"),
        1008 to ("강원특별자치도" to "홍천군"),
        1009 to ("강원특별자치도" to "횡성군"),
        1010 to ("강원특별자치도" to "영월군"),
        1011 to ("강원특별자치도" to "평창군"),
        1012 to ("강원특별자치도" to "정선군"),
        1013 to ("강원특별자치도" to "철원군"),
        1014 to ("강원특별자치도" to "화천군"),
        1015 to ("강원특별자치도" to "양구군"),
        1016 to ("강원특별자치도" to "인제군"),
        1017 to ("강원특별자치도" to "고성군"),
        1018 to ("강원특별자치도" to "양양군"),
        1101 to ("충청북도" to "청주시"),
        1102 to ("충청북도" to "충주시"),
        1103 to ("충청북도" to "제천시"),
        1104 to ("충청북도" to "보은군"),
        1105 to ("충청북도" to "옥천군"),
        1106 to ("충청북도" to "영동군"),
        1107 to ("충청북도" to "진천군"),
        1108 to ("충청북도" to "괴산군"),
        1109 to ("충청북도" to "음성군"),
        1110 to ("충청북도" to "단양군"),
        1111 to ("충청북도" to "증평군"),
        1201 to ("충청남도" to "천안시"),
        1202 to ("충청남도" to "공주시"),
        1203 to ("충청남도" to "보령시"),
        1204 to ("충청남도" to "아산시"),
        1205 to ("충청남도" to "서산시"),
        1206 to ("충청남도" to "논산시"),
        1207 to ("충청남도" to "계룡시"),
        1208 to ("충청남도" to "당진시"),
        1209 to ("충청남도" to "금산군"),
        1210 to ("충청남도" to "부여군"),
        1211 to ("충청남도" to "서천군"),
        1212 to ("충청남도" to "청양군"),
        1213 to ("충청남도" to "홍성군"),
        1214 to ("충청남도" to "예산군"),
        1215 to ("충청남도" to "태안군"),
        1301 to ("전라북도" to "전주시"),
        1302 to ("전라북도" to "군산시"),
        1303 to ("전라북도" to "익산시"),
        1304 to ("전라북도" to "정읍시"),
        1305 to ("전라북도" to "남원시"),
        1306 to ("전라북도" to "김제시"),
        1307 to ("전라북도" to "완주군"),
        1308 to ("전라북도" to "진안군"),
        1309 to ("전라북도" to "무주군"),
        1310 to ("전라북도" to "장수군"),
        1311 to ("전라북도" to "임실군"),
        1312 to ("전라북도" to "순창군"),
        1313 to ("전라북도" to "고창군"),
        1314 to ("전라북도" to "부안군"),
        1401 to ("전라남도" to "목포시"),
        1402 to ("전라남도" to "여수시"),
        1403 to ("전라남도" to "순천시"),
        1404 to ("전라남도" to "나주시"),
        1405 to ("전라남도" to "광양시"),
        1406 to ("전라남도" to "담양군"),
        1407 to ("전라남도" to "곡성군"),
        1408 to ("전라남도" to "구례군"),
        1409 to ("전라남도" to "고흥군"),
        1410 to ("전라남도" to "보성군"),
        1411 to ("전라남도" to "화순군"),
        1412 to ("전라남도" to "장흥군"),
        1413 to ("전라남도" to "강진군"),
        1414 to ("전라남도" to "해남군"),
        1415 to ("전라남도" to "영암군"),
        1416 to ("전라남도" to "무안군"),
        1417 to ("전라남도" to "함평군"),
        1418 to ("전라남도" to "영광군"),
        1419 to ("전라남도" to "장성군"),
        1420 to ("전라남도" to "완도군"),
        1421 to ("전라남도" to "진도군"),
        1422 to ("전라남도" to "신안군"),
        1501 to ("경상북도" to "포항시"),
        1502 to ("경상북도" to "경주시"),
        1503 to ("경상북도" to "김천시"),
        1504 to ("경상북도" to "안동시"),
        1505 to ("경상북도" to "구미시"),
        1506 to ("경상북도" to "영주시"),
        1507 to ("경상북도" to "영천시"),
        1508 to ("경상북도" to "상주시"),
        1509 to ("경상북도" to "문경시"),
        1510 to ("경상북도" to "경산시"),
        1511 to ("경상북도" to "의성군"),
        1512 to ("경상북도" to "청송군"),
        1513 to ("경상북도" to "영양군"),
        1514 to ("경상북도" to "영덕군"),
        1515 to ("경상북도" to "청도군"),
        1516 to ("경상북도" to "고령군"),
        1517 to ("경상북도" to "성주군"),
        1518 to ("경상북도" to "칠곡군"),
        1519 to ("경상북도" to "예천군"),
        1520 to ("경상북도" to "봉화군"),
        1521 to ("경상북도" to "울진군"),
        1522 to ("경상북도" to "울릉군"),
        1601 to ("경상남도" to "창원시"),
        1602 to ("경상남도" to "진주시"),
        1603 to ("경상남도" to "통영시"),
        1604 to ("경상남도" to "사천시"),
        1605 to ("경상남도" to "김해시"),
        1606 to ("경상남도" to "밀양시"),
        1607 to ("경상남도" to "거제시"),
        1608 to ("경상남도" to "양산시"),
        1609 to ("경상남도" to "의령군"),
        1610 to ("경상남도" to "함안군"),
        1611 to ("경상남도" to "창녕군"),
        1612 to ("경상남도" to "고성군"),
        1613 to ("경상남도" to "남해군"),
        1614 to ("경상남도" to "하동군"),
        1615 to ("경상남도" to "산청군"),
        1616 to ("경상남도" to "함양군"),
        1617 to ("경상남도" to "거창군"),
        1618 to ("경상남도" to "합천군"),
        1701 to ("제주특별자치도" to "제주시"),
        1702 to ("제주특별자치도" to "서귀포시")
    )

    // 대분류 목록 (시/도)
    val cities = regionData.values.map { it.first }.distinct().sorted()

    // 중분류 목록 (선택된 시/도에 해당하는 구/군)
    val districts = if (selectedRegion.isNotEmpty()) {
        regionData.values.filter { it.first == selectedRegion }.map { it.second }.sorted()
    } else {
        emptyList()
    }

    fun getRegionCode(): Int? {
        return regionData.entries.find {
            it.value.first == selectedRegion && it.value.second == selectedDistrict
        }?.key
    }

    fun updateAllAgreements(checked: Boolean) {
        agreeAll = checked
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
        val tag = "🔍 디버깅: PersonalInfoScreen"
        Log.d(tag, "=== 개인정보 입력 완료 준비 ===")

        val regionCode = getRegionCode()
        Log.d(tag, "지역 코드 계산 결과: $regionCode")
        Log.d(tag, "  - 선택된 시/도: $selectedRegion")
        Log.d(tag, "  - 선택된 구/군: $selectedDistrict")

        if (regionCode == null) {
            Log.e(tag, "❌ 지역 코드가 null입니다. 함수 종료")
            return
        }

        val personalInfo = PersonalInfoData(
            name = name,
            phone = phoneNumber,
            regionCode = regionCode,
            agreements = AgreementsData(
                terms = agreeTerms,
                privacy = agreePrivacyTerms,
                over14 = agreeAgeTerms,
                marketing = MarketingAgreementsData(
                    sms = agreeSMS,
                    push = agreeAppPush,
                    email = agreeEmail
                )
            )
        )

        Log.d(tag, "개인정보 데이터 생성 완료:")
        Log.d(tag, "  - 이름: ${personalInfo.name}")
        Log.d(tag, "  - 전화번호: ${personalInfo.phone}")
        Log.d(tag, "  - 지역코드: ${personalInfo.regionCode}")
        Log.d(
            tag,
            "  - 필수약관: 이용약관=${personalInfo.agreements.terms}, 개인정보=${personalInfo.agreements.privacy}, 만14세=${personalInfo.agreements.over14}"
        )
        Log.d(
            tag,
            "  - 마케팅약관: SMS=${personalInfo.agreements.marketing.sms}, 푸시=${personalInfo.agreements.marketing.push}, 이메일=${personalInfo.agreements.marketing.email}"
        )

        temporaryUserId?.let { userId ->
            Log.d(
                tag,
                "✅ 임시 사용자 ID 확인됨: $userId"
            )
            Log.d(
                tag,
                "🚀 ViewModel.completePersonalInfo() 호출 시작..."
            )
            viewModel.completePersonalInfo(userId.toInt(), personalInfo)
        } ?: run {
            Log.e(
                tag,
                "❌ temporaryUserId가 null입니다!"
            )
        }
    }

    LaunchedEffect(uiState.isSuccess) {
        val tag = "🔍 디버깅: PersonalInfoScreen"
        Log.d(tag, "LaunchedEffect 트리거됨")
        Log.d(tag, "  - uiState.isSuccess: ${uiState.isSuccess}")
        Log.d(tag, "  - uiState.isLoading: ${uiState.isLoading}")
        Log.d(tag, "  - uiState.errorMessage: ${uiState.errorMessage}")
        Log.d(tag, "  - uiState.user: ${uiState.user}")

        if (uiState.isSuccess) {
            Log.d(tag, "🎉 isSuccess가 true이므로 onCompleteClick() 호출")
            onCompleteClick()
        } else {
            Log.d(tag, "ℹ️ isSuccess가 false이므로 아무 작업 안함")
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
                    // CommonTopBar 추가
                    CommonTopBar(
                        title = "회원가입",
                        onBackClick = onBackClick,

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
                        Spacer(modifier = Modifier.height(24.dp))
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
                        Spacer(modifier = Modifier.height(24.dp))

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

                        Row(modifier = Modifier.fillMaxWidth()) {
                            LightonDropdown(
                                label = "",
                                selectedItem = selectedRegion,
                                items = cities,
                                onItemSelected = {
                                    selectedRegion = it
                                    selectedDistrict = "" // 대분류 변경시 중분류 리셋
                                },
                                placeholder = "대분류",
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
                                placeholder = "중분류",
                                isRequired = false,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "마케팅 정보 수신 (선택)",
                            fontFamily = PretendardFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(top = 24.dp, bottom = 20.dp)
                        )

                        // 출입 방법 저장
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 20.dp)
                        ) {
                            LightonCheckbox(
                                text = "",
                                isChecked = agreeEntrance,
                                onCheckedChange = { agreeEntrance = it }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "출입 방법 저장",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.Black,
                                fontFamily = PretendardFamily,
                                letterSpacing = (-0.5).sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // SMS, 앱 푸시, 이메일 한 줄에 배치
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 0.dp, end = 20.dp)
                        ) {
                            // SMS
                            LightonCheckbox(
                                text = "",
                                isChecked = agreeSMS,
                                onCheckedChange = { agreeSMS = it }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "SMS",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.Black,
                                fontFamily = PretendardFamily,
                                letterSpacing = (-1).sp
                            )

                            Spacer(modifier = Modifier.width(18.dp))

                            // 앱 푸시
                            LightonCheckbox(
                                text = "",
                                isChecked = agreeAppPush,
                                onCheckedChange = { agreeAppPush = it }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "앱 푸시",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.Black,
                                fontFamily = PretendardFamily,
                                letterSpacing = (-0.5).sp
                            )

                            Spacer(modifier = Modifier.width(18.dp))

                            // 이메일
                            LightonCheckbox(
                                text = "",
                                isChecked = agreeEmail,
                                onCheckedChange = { agreeEmail = it }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "이메일",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.Black,
                                fontFamily = PretendardFamily,
                                letterSpacing = (-0.5).sp
                            )
                        }

                        Text(
                            text = "* 수신 동의 상태는 개인 설정에서 별도로 변경할 수 있습니다.",
                            fontFamily = PretendardFamily,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp, bottom = 8.dp),
                            letterSpacing = (-0.5).sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "약관 동의",
                            fontFamily = PretendardFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(top = 24.dp, bottom = 20.dp)
                        )

                        // 전체 동의
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 20.dp)
                        ) {
                            LightonCheckbox(
                                text = "",
                                isChecked = agreeAll,
                                onCheckedChange = { updateAllAgreements(it) }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "전체 동의합니다.",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = CaptionColor,
                                fontFamily = PretendardFamily,
                                letterSpacing = (-0.5).sp,
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "",
                                fontSize = 12.sp,
                                color = CaptionColor,
                                fontWeight = FontWeight.Normal,
                                fontFamily = PretendardFamily,
                                textDecoration = TextDecoration.Underline,
                                modifier = Modifier.clickable { /* TODO: 상세 팝업 */ },
                                letterSpacing = (-1).sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // 하위 약관 3줄
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 20.dp)
                        ) {
                            LightonCheckbox(
                                text = "",
                                isChecked = agreeTerms,
                                onCheckedChange = { agreeTerms = it },
                                modifier = Modifier.padding(start = 13.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "이용약관에 동의 합니다. (필수)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.Black,
                                fontFamily = PretendardFamily,
                                letterSpacing = (-0.5).sp,
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "내용보기",
                                fontSize = 12.sp,
                                color = CaptionColor,
                                fontWeight = FontWeight.Normal,
                                fontFamily = PretendardFamily,
                                textDecoration = TextDecoration.Underline,
                                modifier = Modifier.clickable { /* TODO: 상세 팝업 */ },
                                letterSpacing = (-1).sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 20.dp)
                        ) {
                            LightonCheckbox(
                                text = "",
                                isChecked = agreePrivacyTerms,
                                onCheckedChange = { agreePrivacyTerms = it },
                                modifier = Modifier.padding(start = 13.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "개인정보 수집 및 이용에 동의합니다. (필수)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.Black,
                                fontFamily = PretendardFamily,
                                letterSpacing = (-0.8).sp,
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "내용보기",
                                fontSize = 12.sp,
                                color = CaptionColor,
                                fontWeight = FontWeight.Normal,
                                fontFamily = PretendardFamily,
                                textDecoration = TextDecoration.Underline,
                                modifier = Modifier.clickable { /* TODO: 상세 팝업 */ },
                                letterSpacing = (-1).sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 20.dp)
                        ) {
                            LightonCheckbox(
                                text = "",
                                isChecked = agreeAgeTerms,
                                onCheckedChange = { agreeAgeTerms = it },
                                modifier = Modifier.padding(start = 13.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "만 14세 이상입니다. (필수)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.Black,
                                fontFamily = PretendardFamily,
                                letterSpacing = (-0.5).sp,
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))
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
                                    val tag = "🔍 디버깅: PersonalInfoScreen"
                                    Log.d(tag, "=== 버튼 클릭됨 ===")
                                    Log.d(tag, "  - temporaryUserId: $temporaryUserId")
                                    Log.d(tag, "  - isFormValid: $isFormValid")
                                    Log.d(tag, "  - name: '$name'")
                                    Log.d(tag, "  - phoneNumber: '$phoneNumber'")
                                    Log.d(tag, "  - isPhoneVerified: $isPhoneVerified")
                                    Log.d(tag, "  - selectedRegion: '$selectedRegion'")
                                    Log.d(tag, "  - selectedDistrict: '$selectedDistrict'")
                                    Log.d(tag, "  - agreeTerms: $agreeTerms")
                                    Log.d(tag, "  - agreePrivacyTerms: $agreePrivacyTerms")
                                    Log.d(tag, "  - agreeAgeTerms: $agreeAgeTerms")

                                    if (temporaryUserId != null) {
                                        Log.d(
                                            tag,
                                            "🚀 개인정보 입력 완료 함수 호출 (temporaryUserId: $temporaryUserId)"
                                        )
                                        completePersonalInfo()
                                    } else {
                                        Log.d(tag, "⚠️ temporaryUserId가 null입니다. onNextClick 호출")
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
        PersonalInfoScreen(temporaryUserId = "123")
    }
}
