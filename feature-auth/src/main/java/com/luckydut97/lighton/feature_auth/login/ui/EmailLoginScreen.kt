package com.luckydut97.lighton.feature_auth.login.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.lighton.core.ui.components.LightonButton
import com.luckydut97.lighton.core.ui.theme.BrandColor
import com.luckydut97.lighton.core.ui.theme.LightonTheme
import com.luckydut97.lighton.core.ui.theme.PlaceholderTextColor
import com.luckydut97.lighton.core.ui.theme.PretendardFamily
import com.luckydut97.lighton.core.ui.theme.TextFieldBackgroundColor
import com.luckydut97.lighton.core.ui.theme.TextFieldBorderColor
import com.luckydut97.lighton.feature.auth.R

@Composable
fun EmailLoginScreen(
    onBackClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onKakaoLoginClick: () -> Unit = {},
    onGoogleLoginClick: () -> Unit = {},
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
                // 뒤로가기 버튼 - 왼쪽 상단에 위치시키고 약간 여백 추가
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .absoluteOffset(x = (-4).dp, y = 8.dp)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "뒤로가기",
                        tint = Color.Black
                    )
                }

                // 로고
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
                        // 아이디(이메일) 입력 필드
                        Text(
                            text = "아이디",
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = PretendardFamily,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = {
                                Text(
                                    "아이디 (이메일 주소)",
                                    color = PlaceholderTextColor,
                                    fontFamily = PretendardFamily
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(TextFieldBackgroundColor, RoundedCornerShape(8.dp)),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrandColor,
                                unfocusedBorderColor = TextFieldBorderColor,
                                focusedContainerColor = TextFieldBackgroundColor,
                                unfocusedContainerColor = TextFieldBackgroundColor
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // 비밀번호 입력 필드
                        Text(
                            text = "비밀번호",
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = PretendardFamily,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = {
                                Text(
                                    "비밀번호",
                                    color = PlaceholderTextColor,
                                    fontFamily = PretendardFamily
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(TextFieldBackgroundColor, RoundedCornerShape(8.dp)),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrandColor,
                                unfocusedBorderColor = TextFieldBorderColor,
                                focusedContainerColor = TextFieldBackgroundColor,
                                unfocusedContainerColor = TextFieldBackgroundColor
                            ),
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
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

                        // 아이디찾기, 비밀번호 찾기 링크
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "아이디 찾기",
                                modifier = Modifier
                                    .clickable { onFindIdClick() }
                                    .padding(horizontal = 8.dp),
                                fontSize = 14.sp,
                                fontFamily = PretendardFamily,
                                style = MaterialTheme.typography.labelSmall
                            )

                            Text(
                                text = "|",
                                color = Color.Gray,
                                fontFamily = PretendardFamily,
                                style = MaterialTheme.typography.labelSmall
                            )

                            Text(
                                text = "비밀번호 찾기",
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