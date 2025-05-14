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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.lighton.core.ui.components.LightonBackButton
import com.luckydut97.lighton.core.ui.components.LightonButton
import com.luckydut97.lighton.core.ui.components.LightonInputField
import com.luckydut97.lighton.core.ui.theme.LightonTheme
import com.luckydut97.lighton.core.ui.theme.PretendardFamily
import com.luckydut97.lighton.feature.auth.R

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

    LightonTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                LightonBackButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 17.dp, top = 32.dp)
                )

                // 로고 - 뒤로가기 버튼과 입력 필드 사이에 정확히 위치
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_type_purple),
                        contentDescription = "Light On 로고",
                        modifier = Modifier
                            .width(140.dp)
                            .height(60.dp)
                    )
                }

                // 메인 콘텐츠 영역
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 로고 아래 여백
                    Spacer(modifier = Modifier.height(160.dp))

                    // 중앙 콘텐츠 - 입력 필드, 버튼
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // 아이디(이메일) 입력 필드 - LightonInputField 컴포넌트 사용
                        LightonInputField(
                            label = "아이디",
                            value = email,
                            onValueChange = { email = it },
                            placeholder = "아이디 (이메일 주소)",
                            keyboardType = KeyboardType.Email
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // 비밀번호 입력 필드 - LightonInputField 컴포넌트 사용
                        LightonInputField(
                            label = "비밀번호",
                            value = password,
                            onValueChange = { password = it },
                            placeholder = "비밀번호",
                            keyboardType = KeyboardType.Password,
                            isPassword = true
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // 로그인 버튼
                        LightonButton(
                            text = "로그인",
                            modifier = Modifier.height(47.dp),
                            onClick = onLoginClick
                        )
                    }

                    // 하단 섹션 (또는, 소셜 로그인, 링크)
                    Column(
                        modifier = Modifier
                            .weight(0.4f)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        // 여백 추가
                        Spacer(modifier = Modifier.height(4.dp))

                        // 또는 텍스트
                        Text(
                            text = "또는",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontFamily = PretendardFamily
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // 소셜 로그인 버튼들
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            // 카카오 로그인 버튼
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(Color.White, CircleShape)
                                    .clickable { onKakaoLoginClick() }
                                    .border(width = 0.5.dp, color = Color.LightGray, shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_kakao_logo),
                                    contentDescription = "카카오 로그인",
                                    modifier = Modifier
                                        .size(28.dp)
                                        .padding(4.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(24.dp))

                            // 구글 로그인 버튼
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(Color.White, CircleShape)
                                    .clickable { onGoogleLoginClick() }
                                    .border(width = 0.5.dp, color = Color.LightGray, shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_google_logo),
                                    contentDescription = "구글 로그인",
                                    modifier = Modifier
                                        .size(28.dp)
                                        .padding(4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // 회원가입, 아이디찾기, 비밀번호 찾기 링크
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "회원가입",
                                color = Color.Black,
                                modifier = Modifier
                                    .clickable { onSignUpClick() }
                                    .padding(horizontal = 8.dp),
                                fontSize = 14.sp,
                                fontFamily = PretendardFamily,
                                style = MaterialTheme.typography.labelSmall
                            )

                            Box(
                                modifier = Modifier
                                    .height(12.dp)
                                    .width(1.dp)
                                    .background(Color.LightGray)
                            )

                            Text(
                                text = "아이디 찾기",
                                color = Color.Black,
                                modifier = Modifier
                                    .clickable { onFindIdClick() }
                                    .padding(horizontal = 8.dp),
                                fontSize = 14.sp,
                                fontFamily = PretendardFamily,
                                style = MaterialTheme.typography.labelSmall
                            )

                            Box(
                                modifier = Modifier
                                    .height(12.dp)
                                    .width(1.dp)
                                    .background(Color.LightGray)
                            )

                            Text(
                                text = "비밀번호 찾기",
                                color = Color.Black,
                                modifier = Modifier
                                    .clickable { onFindPasswordClick() }
                                    .padding(horizontal = 8.dp),
                                fontSize = 14.sp,
                                fontFamily = PretendardFamily,
                                style = MaterialTheme.typography.labelSmall
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