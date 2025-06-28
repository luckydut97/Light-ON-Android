package com.luckydut97.lighton.feature_auth.login.ui

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.luckydut97.lighton.core.ui.components.LightonBackButton
import com.luckydut97.lighton.core.ui.theme.LightonTheme
import com.luckydut97.lighton.core.ui.theme.PretendardFamily

/**
 * 소셜 로그인 결과
 */
sealed class SocialLoginResult {
    data class Success(val code: String) : SocialLoginResult()
    data class Error(val message: String) : SocialLoginResult()
    data object Cancelled : SocialLoginResult()
}

/**
 * 소셜 로그인 WebView 화면
 */
@Composable
fun SocialLoginWebView(
    title: String = "소셜 로그인",
    authUrl: String,
    callbackUrlPrefix: String, // 예: "https://api.lighton.com/oauth/kakao/callback"
    onResult: (SocialLoginResult) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    // WebView 설정
    val webView = remember {
        WebView(context).apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                setSupportZoom(false)
                builtInZoomControls = false
                displayZoomControls = false
            }

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    url?.let { currentUrl ->
                        // 콜백 URL 감지
                        if (currentUrl.startsWith(callbackUrlPrefix)) {
                            handleCallbackUrl(currentUrl, onResult)
                            return true
                        }
                    }
                    return false
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    url?.let { currentUrl ->
                        // 페이지 로딩 완료 후에도 콜백 URL 확인
                        if (currentUrl.startsWith(callbackUrlPrefix)) {
                            handleCallbackUrl(currentUrl, onResult)
                        }
                    }
                }
            }
        }
    }

    LightonTheme {
        Scaffold { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // 헤더
                LightonBackButton(
                    onClick = {
                        onResult(SocialLoginResult.Cancelled)
                        onBackClick()
                    },
                    modifier = Modifier.padding(16.dp)
                )

                Text(
                    text = title,
                    fontFamily = PretendardFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                // WebView
                AndroidView(
                    factory = { webView },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    update = { view ->
                        view.loadUrl(authUrl)
                    }
                )
            }
        }
    }
}

/**
 * 콜백 URL에서 인가 코드 추출
 */
private fun handleCallbackUrl(
    callbackUrl: String,
    onResult: (SocialLoginResult) -> Unit
) {
    try {
        val uri = android.net.Uri.parse(callbackUrl)

        // 에러가 있는지 확인
        val error = uri.getQueryParameter("error")
        if (error != null) {
            val errorDescription = uri.getQueryParameter("error_description") ?: "소셜 로그인에 실패했습니다."
            onResult(SocialLoginResult.Error(errorDescription))
            return
        }

        // 인가 코드 추출
        val code = uri.getQueryParameter("code")
        if (code != null) {
            onResult(SocialLoginResult.Success(code))
        } else {
            onResult(SocialLoginResult.Error("인가 코드를 받을 수 없습니다."))
        }
    } catch (e: Exception) {
        onResult(SocialLoginResult.Error("콜백 URL 처리 중 오류가 발생했습니다: ${e.message}"))
    }
}