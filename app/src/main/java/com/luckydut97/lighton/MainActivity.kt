package com.luckydut97.lighton

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.luckydut97.lighton.core.ui.theme.LightonTheme
import com.luckydut97.lighton.data.storage.TokenManagerImpl
import com.luckydut97.lighton.data.repository.SessionRepositoryImpl
import com.luckydut97.lighton.data.repository.AuthState
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val tag = "🔍 디버깅: MainActivity"

    companion object {
        var globalSessionRepository: SessionRepositoryImpl? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Log.d(tag, "=== MainActivity 시작 ===")

        // 시스템바 설정
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // 상태바와 네비게이션바 아이콘을 어두운 테마로 설정 (흰색 배경에 어울리도록)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
            isAppearanceLightNavigationBars = true
        }

        // SessionRepository 초기화
        val tokenManager = TokenManagerImpl(this)
        globalSessionRepository = SessionRepositoryImpl(tokenManager)

        Log.d(tag, "SessionRepository 초기화 완료")

        // UserState와 SessionRepository 동기화 (lifecycleScope + repeatOnLifecycle로 중복 launch 방지)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                globalSessionRepository?.authState?.collect { authState ->
                    Log.d(
                        tag,
                        "[동기화] 인증 상태 변경 감지: $authState, UserState.isLoggedIn: ${UserState.isLoggedIn}"
                    )
                    UserState.isLoggedIn = when (authState) {
                        is AuthState.Authenticated -> {
                            Log.d(tag, "✅ 로그인 상태로 UserState 업데이트 (동기화)")
                            true
                        }

                        else -> {
                            Log.d(tag, "❌ 비로그인 상태로 UserState 업데이트 (동기화)")
                            false
                        }
                    }
                    Log.d(tag, "[동기화] 현재 UserState.isLoggedIn: ${UserState.isLoggedIn}")
                }
            }
        }

        setContent {
            LightonTheme {
                AppNavigation()
            }
        }
    }
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
