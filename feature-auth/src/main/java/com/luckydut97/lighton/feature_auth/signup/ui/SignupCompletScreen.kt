package com.luckydut97.lighton.feature_auth.signup.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.lighton.core.ui.components.LightonButton
import com.luckydut97.lighton.core.ui.theme.LightonTheme
import com.luckydut97.lighton.core.ui.theme.PretendardFamily
import com.luckydut97.lighton.feature.auth.R

/**
 * 회원가입 완료 화면
 * 회원가입을 축하하는 메시지와 함께 앱 사용 시작 안내
 */
@Composable
fun SignupCompleteScreen(
    onConfirmClick: () -> Unit = {}
) {
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
                    // 상단 닫기 버튼
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, end = 16.dp)
                    ) {
                        // X 버튼
                        Text(
                            text = "×",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .clickable { onConfirmClick() }
                                .padding(8.dp)
                        )
                    }

                    // 중앙 콘텐츠
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    ) {
                        // 로고 이미지
                        Image(
                            painter = painterResource(id = R.drawable.ic_type_purple),
                            contentDescription = "Light On 로고",
                            modifier = Modifier
                                .size(120.dp)
                                .padding(bottom = 40.dp)
                        )

                        // 축하 메시지
                        Text(
                            text = "회원가입을 축하드립니다!",
                            fontFamily = PretendardFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // 부가 설명
                        Text(
                            text = "라이트온과 함께\n즐거운 공연을 즐겨보세요",
                            fontFamily = PretendardFamily,
                            fontSize = 16.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            lineHeight = 24.sp
                        )
                    }

                    // 하단 버튼
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        LightonButton(
                            text = "확인",
                            onClick = onConfirmClick,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignupCompleteScreenPreview() {
    LightonTheme {
        SignupCompleteScreen()
    }
}