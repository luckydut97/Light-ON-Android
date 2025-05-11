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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.lighton.feature.auth.R

@Composable
fun LoginScreen(
    onBackClick: () -> Unit = {},
    onKakaoLoginClick: () -> Unit = {},
    onNaverLoginClick: () -> Unit = {},
    onEmailLoginClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onFindIdClick: () -> Unit = {},
    onFindPasswordClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            /*// 뒤로 가기 버튼
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.Start)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "뒤로 가기"
                )
            }
            */
            Spacer(modifier = Modifier.height(16.dp))
            
            // 로고 영역
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_type_purple),
                    contentDescription = "Light On 로고",
                    modifier = Modifier
                        .height(180.dp)
                        .width(180.dp) // 원하는 크기로 조정
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 가입 안내 텍스트
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_login_noti),
                    contentDescription = "로그인 안내",
                    modifier = Modifier.fillMaxWidth(1.0f)
                )
            }
            
            // 소셜 로그인 버튼들
            // 카카오 로그인
            Button(
                onClick = onKakaoLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFEE500) // 카카오 색상
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color.Transparent)
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .background(Color.White, RoundedCornerShape(2.dp))
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "카카오로 3초 만에 로그인",
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 네이버 로그인
            Button(
                onClick = onNaverLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF03C75A) // 네이버 색상
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color.Transparent)
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .background(Color.White, RoundedCornerShape(2.dp))
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "네이버로 3초 만에 로그인",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 이메일 로그인
            OutlinedButton(
                onClick = onEmailLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "이메일로 로그인",
                    fontSize = 16.sp
                )
            }
            
            Spacer(modifier = Modifier.height(18.dp))
            
            // "또는" 구분선
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier.weight(1f),
                    thickness = 0.5.dp,
                    color = Color.LightGray
                )
                Text(
                    text = "또는",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Divider(
                    modifier = Modifier.weight(1f),
                    thickness = 0.5.dp,
                    color = Color.LightGray
                )
            }
            
            Spacer(modifier = Modifier.height(18.dp))
            
            // 원형 버튼들
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.White, CircleShape)
                        .clickable {  }
                        .border(width = 0.5.dp, color = Color.LightGray, shape = CircleShape)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_kakao_logo),
                        contentDescription = "카카오 로고",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(24.dp))
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.White, CircleShape)
                        .clickable { onNaverLoginClick() }
                        .border(width = 0.5.dp, color = Color.LightGray, shape = CircleShape)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_google_logo),
                        contentDescription = "구글 로고",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            /*Spacer(modifier = Modifier.weight(1f))*/
            Spacer(modifier = Modifier.height(18.dp))

            // 하단 링크들
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "회원가입",
                    modifier = Modifier
                        .clickable { onSignUpClick() }
                        .padding(horizontal = 8.dp),
                    fontSize = 14.sp
                )
                Text(text = "|", color = Color.Gray)
                Text(
                    text = "아이디 찾기",
                    modifier = Modifier
                        .clickable { onFindIdClick() }
                        .padding(horizontal = 8.dp),
                    fontSize = 14.sp
                )
                Text(text = "|", color = Color.Gray)
                Text(
                    text = "비밀번호 찾기",
                    modifier = Modifier
                        .clickable { onFindPasswordClick() }
                        .padding(horizontal = 8.dp),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}