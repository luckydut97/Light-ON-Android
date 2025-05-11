package com.luckydut97.lighton.feature_auth.splash.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luckydut97.lighton.core.ui.theme.LightonTheme  // 추가: 테마 import
import com.luckydut97.lighton.feature.auth.R
import com.luckydut97.lighton.feature_auth.splash.viewmodel.SplashViewModel

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = viewModel(),
    onNavigateToLogin: () -> Unit
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val navigateToLogin by viewModel.navigateToLogin.collectAsState()

    LaunchedEffect(navigateToLogin) {
        if (navigateToLogin) {
            onNavigateToLogin()
        }
    }

    SplashContent()
}

@Composable
fun SplashContent() {
    // 디자인에 맞는 보라색 배경
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6200EE)) // 보라색 배경
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 로고 이미지
            Image(
                painter = painterResource(id = R.drawable.ic_typo_white),
                contentDescription = "LightOn Logo",
                modifier = Modifier.size(247.dp)
            )

            Spacer(modifier = Modifier.height(1.dp))

            // 텍스트 - MaterialTheme.typography 사용
            Text(
                text = "전국의 모든 공연 정보",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium, // Pretendard 폰트 적용
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 50.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    LightonTheme {  // 추가: 테마 적용
        SplashContent()
    }
}