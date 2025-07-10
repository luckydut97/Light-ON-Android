package com.luckydut97.lighton.feature_auth.login.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowInsetsCompat
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luckydut97.lighton.core.ui.components.LightonButton
import com.luckydut97.lighton.core.ui.components.LoginInputField
import com.luckydut97.lighton.core.ui.components.CommonTopBar
import com.luckydut97.lighton.core.ui.theme.AssistiveColor
import com.luckydut97.lighton.core.ui.theme.BrandColor
import com.luckydut97.lighton.core.ui.theme.ClickableColor
import com.luckydut97.lighton.core.ui.theme.InfoTextColor
import com.luckydut97.lighton.core.ui.theme.LightonTheme
import com.luckydut97.lighton.core.ui.theme.PretendardFamily
import com.luckydut97.lighton.feature.auth.R
import com.luckydut97.lighton.feature_auth.login.viewmodel.LoginViewModel
import com.luckydut97.domain.model.User
import kotlin.math.min

@Composable
fun EmailLoginScreen(
    onBackClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onLoginSuccess: (User, String, String) -> Unit = { _, _, _ -> },
    onKakaoLoginClick: () -> Unit = {},
    onGoogleLoginClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onFindIdClick: () -> Unit = {},
    onFindPasswordClick: () -> Unit = {},
    viewModel: LoginViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isEmailFocused by remember { mutableStateOf(false) }
    var isPasswordFocused by remember { mutableStateOf(false) }

    // ViewModel 상태 관찰
    val uiState by viewModel.uiState.collectAsState()

    // 로그인 성공 시 콜백 호출
    LaunchedEffect(uiState.isSuccess, uiState.user) {
        val user = uiState.user
        if (uiState.isSuccess && user != null) {
            // 실제 토큰 정보는 아직 도메인에 구현되지 않았으므로 임시로 하드코딩
            // 추후 서버 연동 완성 시 user 데이터와 토큰 실제 반환값 사용
            onLoginSuccess(user, "temp_access_token_from_ui", "temp_refresh_token_from_ui")
        }
    }

    // 에러 메시지 로깅 (디버깅용)
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            println("🔴 로그인 오류: $error")
        }
    }

    // 반응형 디자인을 위한 스케일 팩터 계산
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    // 기준 해상도 대비 비율 계산 (402x874 기준)
    val widthRatio = screenWidth / 402.dp
    val heightRatio = screenHeight / 874.dp
    val scaleFactor = min(widthRatio, heightRatio)

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
                        title = "",
                        onBackClick = onBackClick,

                        )

                    // TopBar와 로고 사이 11dp 간격
                    Spacer(modifier = Modifier.height((80 * scaleFactor).dp))

                    // 메인 콘텐츠 - 위쪽에 위치
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = (20 * scaleFactor).dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 로고 - 140*44.7
                        Image(
                            painter = painterResource(id = R.drawable.ic_type_purple),
                            contentDescription = "Light On 로고",
                            modifier = Modifier
                                .width((140 * scaleFactor).dp)
                                .height((44.7 * scaleFactor).dp)
                        )

                        // 로고와 아이디 입력 필드 사이 11dp 간격
                        Spacer(modifier = Modifier.height((100 * scaleFactor).dp))

                        // 입력 필드 섹션
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // 아이디 입력 필드
                            LoginInputField(
                                label = "아이디",
                                value = email,
                                onValueChange = { email = it },
                                modifier = Modifier
                                    .width((334 * scaleFactor).dp),
                                placeholder = "아이디 (이메일 주소)",
                                isFocused = isEmailFocused,
                                onFocusChanged = { isEmailFocused = it },
                                keyboardType = KeyboardType.Email
                            )

                            // 24dp 간격
                            Spacer(modifier = Modifier.height((24 * scaleFactor).dp))

                            // 비밀번호 입력 필드
                            LoginInputField(
                                label = "비밀번호",
                                value = password,
                                onValueChange = { password = it },
                                modifier = Modifier
                                    .width((334 * scaleFactor).dp),
                                placeholder = "비밀번호",
                                isFocused = isPasswordFocused,
                                onFocusChanged = { isPasswordFocused = it },
                                keyboardType = KeyboardType.Password,
                                isPassword = true
                            )
                        }

                        // 에러 메시지 표시
                        uiState.errorMessage?.let { error ->
                            Spacer(modifier = Modifier.height((8 * scaleFactor).dp))
                            Text(
                                text = error,
                                color = Color.Red,
                                fontSize = (14 * scaleFactor).sp,
                                fontFamily = PretendardFamily,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width((334 * scaleFactor).dp)
                            )
                        }

                        // 23dp 간격
                        Spacer(modifier = Modifier.height((23 * scaleFactor).dp))

                        // 로그인 버튼
                        LightonButton(
                            text = "로그인",
                            modifier = Modifier
                                .width((334 * scaleFactor).dp)
                                .height(47.dp),
                            onClick = {
                                if (email.isNotEmpty() && password.isNotEmpty()) {
                                    println("🚀 로그인 버튼 클릭 - 이메일: $email")
                                    viewModel.login(email, password)
                                } else {
                                    println("⚠️ 이메일 또는 비밀번호가 비어있음")
                                }
                            },
                            borderWidth = -1.dp  // border 제거
                        )

                        // 23dp 간격
                        Spacer(modifier = Modifier.height((23 * scaleFactor).dp))

                        // 또는 텍스트와 선
                        Row(
                            modifier = Modifier.width((334 * scaleFactor).dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // 왼쪽 선
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height((1 * scaleFactor).dp)
                                    .background(Color(0xFFF5F5F5))
                            )

                            // 17dp 여백
                            Spacer(modifier = Modifier.width((17 * scaleFactor).dp))

                            // 또는 텍스트
                            Text(
                                text = "또는",
                                color = AssistiveColor,
                                fontSize = (14 * scaleFactor).sp,
                                fontFamily = PretendardFamily,
                                fontWeight = FontWeight.SemiBold,
                            )

                            // 17dp 여백
                            Spacer(modifier = Modifier.width((17 * scaleFactor).dp))

                            // 오른쪽 선
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height((1 * scaleFactor).dp)
                                    .background(Color(0xFFF5F5F5))
                            )
                        }

                        // 23dp 간격
                        Spacer(modifier = Modifier.height((23 * scaleFactor).dp))

                        // 소셜 로그인 버튼들 - 37*37
                        Row(
                            horizontalArrangement = Arrangement.spacedBy((24 * scaleFactor).dp)
                        ) {
                            // 카카오 로그인 버튼
                            Image(
                                painter = painterResource(id = R.drawable.ic_kakao_logo),
                                contentDescription = "카카오 로그인",
                                modifier = Modifier
                                    .size((37 * scaleFactor).dp)
                                    .clickable { onKakaoLoginClick() }
                            )

                            // 구글 로그인 버튼
                            Image(
                                painter = painterResource(id = R.drawable.ic_google_logo),
                                contentDescription = "구글 로그인",
                                modifier = Modifier
                                    .size((37 * scaleFactor).dp)
                                    .clickable { onGoogleLoginClick() }
                            )
                        }

                        // 23dp 간격
                        Spacer(modifier = Modifier.height((23 * scaleFactor).dp))

                        // 하단 링크들
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "회원가입",
                                color = ClickableColor,
                                modifier = Modifier
                                    .clickable { onSignUpClick() },
                                fontSize = (14 * scaleFactor).sp,
                                fontFamily = PretendardFamily,
                                fontWeight = FontWeight.SemiBold
                            )

                            // 18dp 여백
                            Spacer(modifier = Modifier.width((18 * scaleFactor).dp))

                            Text(
                                text = "|",
                                color = ClickableColor,
                                fontSize = (14 * scaleFactor).sp,
                                fontFamily = PretendardFamily
                            )

                            // 18dp 여백
                            Spacer(modifier = Modifier.width((18 * scaleFactor).dp))

                            Text(
                                text = "아이디 찾기",
                                color = ClickableColor,
                                modifier = Modifier
                                    .clickable {
                                        // 아무일도 안일어나게 함
                                    },
                                fontSize = (14 * scaleFactor).sp,
                                fontFamily = PretendardFamily,
                                fontWeight = FontWeight.SemiBold
                            )

                            // 18dp 여백
                            Spacer(modifier = Modifier.width((18 * scaleFactor).dp))

                            Text(
                                text = "|",
                                color = ClickableColor,
                                fontSize = (14 * scaleFactor).sp,
                                fontFamily = PretendardFamily
                            )

                            // 18dp 여백
                            Spacer(modifier = Modifier.width((18 * scaleFactor).dp))

                            Text(
                                text = "비밀번호 찾기",
                                color = ClickableColor,
                                modifier = Modifier
                                    .clickable {
                                        // 아무일도 안일어나게 함
                                    },
                                fontSize = (14 * scaleFactor).sp,
                                fontFamily = PretendardFamily,
                                fontWeight = FontWeight.SemiBold
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
fun EmailLoginScreenPreview() {
    LightonTheme {
        EmailLoginScreen()
    }
}
