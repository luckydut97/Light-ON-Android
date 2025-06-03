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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.lighton.core.ui.components.LightonBackButton
import com.luckydut97.lighton.core.ui.components.LightonButton
import com.luckydut97.lighton.core.ui.theme.AssistiveColor
import com.luckydut97.lighton.core.ui.theme.BrandColor
import com.luckydut97.lighton.core.ui.theme.ClickableColor
import com.luckydut97.lighton.core.ui.theme.InfoTextColor
import com.luckydut97.lighton.core.ui.theme.LightonTheme
import com.luckydut97.lighton.core.ui.theme.PretendardFamily
import com.luckydut97.lighton.feature.auth.R
import kotlin.math.min

@Composable
fun EmailLoginScreen(
    onBackClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onKakaoLoginClick: () -> Unit = {},
    onGoogleLoginClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onFindIdClick: () -> Unit = {},
    onFindPasswordClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isEmailFocused by remember { mutableStateOf(false) }
    var isPasswordFocused by remember { mutableStateOf(false) }

    // 반응형 디자인을 위한 스케일 팩터 계산
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    // 기준 해상도 대비 비율 계산 (402x874 기준)
    val widthRatio = screenWidth / 402.dp
    val heightRatio = screenHeight / 874.dp
    val scaleFactor = min(widthRatio, heightRatio)

    LightonTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Box(modifier = Modifier.fillMaxSize()) {

                // 전체 콘텐츠 - 상하 중앙 정렬
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = (20 * scaleFactor).dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // 로고 - 140*44.7
                    Image(
                        painter = painterResource(id = R.drawable.ic_type_purple),
                        contentDescription = "Light On 로고",
                        modifier = Modifier
                            .width((140 * scaleFactor).dp)
                            .height((44.7 * scaleFactor).dp)
                    )

                    // 로고 아래 70dp 여백
                    Spacer(modifier = Modifier.height((70 * scaleFactor).dp))

                    // 입력 필드 섹션
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 아이디 섹션
                        Column {
                            // 아이디 타이틀 - 69*23 박스를 입력 필드 위 좌측에 위치
                            Box(
                                modifier = Modifier
                                    .width((334 * scaleFactor).dp)
                                    .height((23 * scaleFactor).dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width((69 * scaleFactor).dp)
                                        .height((23 * scaleFactor).dp)
                                        .align(Alignment.CenterStart),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "아이디",
                                        color = if (isEmailFocused && email.isNotEmpty()) BrandColor else InfoTextColor,
                                        fontSize = (14 * scaleFactor).sp,
                                        fontFamily = PretendardFamily
                                    )
                                }
                            }

                            // 아이디 입력 필드 - 334*47
                            BasicTextField(
                                value = email,
                                onValueChange = { email = it },
                                modifier = Modifier
                                    .width((334 * scaleFactor).dp)
                                    .height((47 * scaleFactor).dp)
                                    .background(
                                        Color.White,
                                        RoundedCornerShape((8 * scaleFactor).dp)
                                    )
                                    .border(
                                        width = (1 * scaleFactor).dp,
                                        color = if (isEmailFocused && email.isNotEmpty()) BrandColor else Color(
                                            0xFFEEEEEE
                                        ),
                                        shape = RoundedCornerShape((8 * scaleFactor).dp)
                                    )
                                    .padding(horizontal = (16 * scaleFactor).dp, vertical = 0.dp)
                                    .onFocusChanged { isEmailFocused = it.isFocused },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                singleLine = true,
                                textStyle = androidx.compose.ui.text.TextStyle(
                                    fontSize = (16 * scaleFactor).sp,
                                    fontFamily = PretendardFamily,
                                    color = Color.Black
                                ),
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        if (email.isEmpty()) {
                                            Text(
                                                text = "아이디 (이메일 주소)",
                                                color = AssistiveColor,
                                                fontSize = (16 * scaleFactor).sp,
                                                fontFamily = PretendardFamily
                                            )
                                        }
                                        innerTextField()
                                    }
                                }
                            )
                        }

                        // 24dp 간격
                        Spacer(modifier = Modifier.height((24 * scaleFactor).dp))

                        // 비밀번호 섹션
                        Column {
                            // 비밀번호 타이틀 - 69*23 박스를 입력 필드 위 좌측에 위치
                            Box(
                                modifier = Modifier
                                    .width((334 * scaleFactor).dp)
                                    .height((23 * scaleFactor).dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width((69 * scaleFactor).dp)
                                        .height((23 * scaleFactor).dp)
                                        .align(Alignment.CenterStart),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "비밀번호",
                                        color = if (isPasswordFocused && password.isNotEmpty()) BrandColor else InfoTextColor,
                                        fontSize = (14 * scaleFactor).sp,
                                        fontFamily = PretendardFamily
                                    )
                                }
                            }

                            // 비밀번호 입력 필드 - 334*47
                            BasicTextField(
                                value = password,
                                onValueChange = { password = it },
                                modifier = Modifier
                                    .width((334 * scaleFactor).dp)
                                    .height((47 * scaleFactor).dp)
                                    .background(
                                        Color.White,
                                        RoundedCornerShape((8 * scaleFactor).dp)
                                    )
                                    .border(
                                        width = (1 * scaleFactor).dp,
                                        color = if (isPasswordFocused && password.isNotEmpty()) BrandColor else Color(
                                            0xFFEEEEEE
                                        ),
                                        shape = RoundedCornerShape((8 * scaleFactor).dp)
                                    )
                                    .padding(horizontal = (16 * scaleFactor).dp, vertical = 0.dp)
                                    .onFocusChanged { isPasswordFocused = it.isFocused },
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                singleLine = true,
                                textStyle = androidx.compose.ui.text.TextStyle(
                                    fontSize = (16 * scaleFactor).sp,
                                    fontFamily = PretendardFamily,
                                    color = Color.Black
                                ),
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        if (password.isEmpty()) {
                                            Text(
                                                text = "비밀번호",
                                                color = AssistiveColor,
                                                fontSize = (16 * scaleFactor).sp,
                                                fontFamily = PretendardFamily
                                            )
                                        }
                                        innerTextField()
                                    }
                                }
                            )
                        }
                    }

                    // 23dp 간격
                    Spacer(modifier = Modifier.height((23 * scaleFactor).dp))

                    // 로그인 버튼
                    LightonButton(
                        text = "로그인",
                        modifier = Modifier
                            .width((334 * scaleFactor).dp)
                            .height((47 * scaleFactor).dp),
                        onClick = onLoginClick
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
                            fontFamily = PretendardFamily
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
                                .clickable { onSignUpClick() }
                                .padding(horizontal = (8 * scaleFactor).dp),
                            fontSize = (14 * scaleFactor).sp,
                            fontFamily = PretendardFamily
                        )

                        Text(
                            text = "|",
                            color = ClickableColor,
                            fontSize = (14 * scaleFactor).sp,
                            fontFamily = PretendardFamily
                        )

                        Text(
                            text = "아이디 찾기",
                            color = ClickableColor,
                            modifier = Modifier
                                .clickable { onFindIdClick() }
                                .padding(horizontal = (8 * scaleFactor).dp),
                            fontSize = (14 * scaleFactor).sp,
                            fontFamily = PretendardFamily
                        )

                        Text(
                            text = "|",
                            color = ClickableColor,
                            fontSize = (14 * scaleFactor).sp,
                            fontFamily = PretendardFamily
                        )

                        Text(
                            text = "비밀번호 찾기",
                            color = ClickableColor,
                            modifier = Modifier
                                .clickable { onFindPasswordClick() }
                                .padding(horizontal = (8 * scaleFactor).dp),
                            fontSize = (14 * scaleFactor).sp,
                            fontFamily = PretendardFamily
                        )
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
