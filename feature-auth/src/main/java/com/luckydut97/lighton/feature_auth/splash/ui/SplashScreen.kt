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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luckydut97.lighton.core.data.repository.AuthState
import com.luckydut97.lighton.core.data.repository.SessionRepository
import com.luckydut97.lighton.core.ui.theme.LightonTheme
import com.luckydut97.lighton.feature_auth.splash.viewmodel.SplashViewModel
import com.luckydut97.lighton.feature.auth.R
import kotlinx.coroutines.delay
import kotlin.math.min

@Composable
fun SplashScreen(
    sessionRepository: SessionRepository,
    onNavigateToMain: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val viewModel: SplashViewModel = viewModel {
        SplashViewModel(sessionRepository)
    }
    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current
    var timerFinished by remember { mutableStateOf(false) }
    var logicFinished by remember { mutableStateOf(false) }

    // 2초 타이머 시작
    LaunchedEffect(Unit) {
        delay(2000)
        timerFinished = true
    }

    // 내부 로직(인증 상태) 완료 체크
    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated
            || authState is AuthState.Unauthenticated
        ) {
            logicFinished = true
        }
    }

    // 둘 다 완료되면 메인 이동
    LaunchedEffect(timerFinished, logicFinished) {
        if (timerFinished && logicFinished) {
            onNavigateToMain()
        }
    }

    SplashContent()
}

@Composable
fun SplashContent() {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    
    // 기준 해상도 대비 비율 계산 (402dp 기준)
    val widthRatio = screenWidth / 402.dp
    val heightRatio = screenHeight / 874.dp
    val scaleFactor = min(widthRatio, heightRatio)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6200EE))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 로고 이미지 - 화면 너비의 30% 사용, 원본 비율 유지 (122*38.75)
            Image(
                painter = painterResource(id = R.drawable.ic_typo_white),
                contentDescription = "LightOn Logo",
                modifier = Modifier
                    .width(screenWidth * 0.3f)
                    .height((screenWidth * 0.3f) * (38.75f / 122f))
            )

            Spacer(modifier = Modifier.height(11.dp * scaleFactor))

            // 텍스트 - Pretendard Bold, 21dp
            Text(
                text = "전국의 모든 공연 정보",
                color = Color.White,
                fontSize = (21 * scaleFactor).sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp * scaleFactor)
            )
            
            Spacer(modifier = Modifier.height(1.dp * scaleFactor))
            
            // 스플래시 아이콘 - 화면 너비의 55% 사용
            Image(
                painter = painterResource(id = R.drawable.ic_splash_icon),
                contentDescription = "Splash Icon",
                modifier = Modifier.size(screenWidth * 0.55f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    LightonTheme {
        SplashContent()
    }
}
