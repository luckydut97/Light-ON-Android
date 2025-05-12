package com.luckydut97.lighton.feature_auth.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.luckydut97.lighton.core.ui.theme.LightonTheme
import com.luckydut97.lighton.feature_auth.login.navigation.LoginNavigation

/**
 * 로그인 관련 화면을 보여주는 액티비티
 * 로그인, 이메일 로그인 화면을 포함합니다.
 */
class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LightonTheme {
                LoginNavigation(
                    onSignUpClick = { 
                        // 회원가입 화면으로 이동하는 로직 (구현 예정)
                    },
                    onLoginSuccess = {
                        // 로그인 성공 시 메인 화면으로 이동하는 로직(구현 예정)
                        finish()
                    },
                    onBackToWelcome = {
                        // 웰컴 화면으로 돌아가는 로직(구현 예정)
                        finish()
                    }
                )
            }
        }
    }
}
