package com.luckydut97.lighton

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.luckydut97.lighton.feature_auth.splash.ui.SplashScreen
import com.luckydut97.lighton.feature_auth.login.navigation.LoginNavigation
import com.luckydut97.lighton.feature_auth.login.navigation.LoginRoute
import com.luckydut97.lighton.core.ui.theme.LightonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LightonTheme {
                // 앱 상태 관리
                var currentScreen by remember { mutableStateOf(Screen.SPLASH) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when (currentScreen) {
                        Screen.SPLASH -> {
                            SplashScreen(
                                onNavigateToLogin = {
                                    currentScreen = Screen.LOGIN
                                }
                            )
                        }
                        Screen.LOGIN -> {
                            // LoginNavigation을 사용하여 로그인 관련 화면 처리
                            LoginNavigation(
                                startDestination = LoginRoute.Login.route,
                                onSignUpClick = {
                                    // 회원가입 화면으로 이동
                                    currentScreen = Screen.SIGNUP
                                },
                                onLoginSuccess = {
                                    // 로그인 성공 시 홈 화면으로 이동
                                    currentScreen = Screen.HOME
                                },
                                onBackToWelcome = {
                                    // 뒤로 가기 버튼 누를 때 메인 액티비티 종료
                                    // 스플래시 화면으로 돌아가지 않고 앱 종료
                                    finish()
                                }
                            )
                        }
                        Screen.HOME -> {
                            Greeting(
                                name = "Android",
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        Screen.SIGNUP, Screen.FINDID, Screen.FINDPASSWORD -> {
                            // 현재는 임시로 동일한 화면 표시
                            // 향후 각 화면 구현 시 변경
                            Text(
                                text = "준비 중인 기능입니다",
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}

enum class Screen {
    SPLASH,
    LOGIN,
    HOME,
    SIGNUP,
    FINDID,
    FINDPASSWORD
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LightonTheme {
        Greeting("Android")
    }
}