package com.luckydut97.lighton.feature_auth.login.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.luckydut97.lighton.feature_auth.login.ui.EmailLoginScreen
import com.luckydut97.lighton.feature_auth.login.ui.LoginScreen
import com.luckydut97.lighton.feature_auth.signup.ui.PersonalInfoScreen
import com.luckydut97.lighton.feature_auth.signup.ui.SignUpScreen

// 로그인 관련 화면 경로 정의
sealed class LoginRoute(val route: String) {
    object Login : LoginRoute("login")
    object EmailLogin : LoginRoute("email_login")
    object SignUp : LoginRoute("sign_up")
    object PersonalInfo : LoginRoute("personal_info") // 추가된 경로
}

/**
 * 로그인 관련 화면 간의 네비게이션을 처리하는 컴포넌트
 */
@Composable
fun LoginNavigation(
    navController: NavHostController = rememberNavController(),
    onSignUpClick: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
    onBackToWelcome: () -> Unit = {},
    startDestination: String = LoginRoute.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(LoginRoute.Login.route) {
            LoginScreen(
                onBackClick = { onBackToWelcome() },
                onEmailLoginClick = {
                    // 이메일 로그인 화면으로 이동 (스택에 쌓이지 않고 대체)
                    navController.navigate(LoginRoute.EmailLogin.route) {
                        popUpTo(LoginRoute.Login.route) { inclusive = true }
                    }
                },
                onKakaoLoginClick = { /* 카카오 로그인 구현 */ },
                onNaverLoginClick = { /* 네이버 로그인 구현 */ },
                onSignUpClick = {
                    navController.navigate(LoginRoute.SignUp.route)
                },
                onFindIdClick = { /* 아이디 찾기 화면으로 이동 구현 */ },
                onFindPasswordClick = { /* 비밀번호 찾기 화면으로 이동 구현 */ }
            )
        }

        composable(LoginRoute.EmailLogin.route) {
            EmailLoginScreen(
                onBackClick = {
                    // 로그인 화면으로 다시 이동 (스택에 쌓이지 않고 대체)
                    navController.navigate(LoginRoute.Login.route) {
                        popUpTo(LoginRoute.EmailLogin.route) { inclusive = true }
                    }
                },
                onLoginClick = onLoginSuccess,
                onKakaoLoginClick = { /* 카카오 로그인 구현 */ },
                onGoogleLoginClick = { /* 구글 로그인 구현 */ },
                onSignUpClick = {
                    navController.navigate(LoginRoute.SignUp.route)
                },
                onFindIdClick = { /* 아이디 찾기 화면으로 이동 구현 */ },
                onFindPasswordClick = { /* 비밀번호 찾기 화면으로 이동 구현 */ }
            )
        }

        composable(LoginRoute.SignUp.route) {
            SignUpScreen(
                onBackClick = {
                    // 회원가입 화면에서 뒤로가기 버튼을 누르면 이전 화면으로 돌아감
                    navController.popBackStack()
                },
                onNextClick = {
                    // 다음 버튼을 누르면 개인정보 입력 화면으로 이동
                    navController.navigate(LoginRoute.PersonalInfo.route)
                }
            )
        }

        // 개인정보 입력 화면 추가
        composable(LoginRoute.PersonalInfo.route) {
            PersonalInfoScreen(
                onBackClick = {
                    // 개인정보 입력 화면에서 뒤로가기 버튼을 누르면 이전 화면으로 돌아감
                    navController.popBackStack()
                },
                onNextClick = {
                    // 회원가입 완료 또는 다음 단계로 이동 구현
                    // 예시: navController.navigate(LoginRoute.SignUpComplete.route)
                    // 또는: onLoginSuccess()
                }
            )
        }
    }
}