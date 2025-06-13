package com.luckydut97.lighton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.luckydut97.lighton.feature_auth.splash.ui.SplashScreen
import com.luckydut97.lighton.feature_home.main.ui.HomeScreen
import com.luckydut97.lighton.feature_map.main.ui.MapScreen
import com.luckydut97.lighton.core.ui.components.BottomNavigationBar
import com.luckydut97.lighton.core.ui.components.NavigationItem
import com.luckydut97.lighton.feature_auth.login.ui.EmailLoginScreen
import com.luckydut97.lighton.feature_auth.signup.ui.SignUpScreen
import com.luckydut97.lighton.feature_auth.signup.ui.PersonalInfoScreen
import com.luckydut97.lighton.feature_auth.signup.ui.MusicPreferenceScreen
import com.luckydut97.lighton.feature_auth.signup.ui.SignupCompleteScreen

/**
 * 앱 전체의 메인 네비게이션을 처리하는 컴포넌트
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    isLoggedIn: Boolean = false
) {
    // 기본 플로우(스플래시 → 로그인 → 메인)
    var startDestination by remember { mutableStateOf(if (isLoggedIn) "main" else "splash") }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // 스플래시 화면
        composable("splash") {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        // 로그인 화면
        composable("login") {
            EmailLoginScreen(
                onBackClick = {
                    // 스플래시로 돌아가거나 앱 종료
                    navController.navigate("splash")
                },
                onLoginClick = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onKakaoLoginClick = { /* 카카오 로그인 구현 */ },
                onGoogleLoginClick = { /* 구글 로그인 구현 */ },
                onSignUpClick = {
                    navController.navigate("signup")
                },
                onFindIdClick = {
                    navController.navigate("findid")
                },
                onFindPasswordClick = {
                    navController.navigate("findpassword")
                }
            )
        }

        // 회원가입 화면
        composable("signup") {
            SignUpScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNextClick = {
                    navController.navigate("personal_info")
                }
            )
        }

        // 개인정보 입력 화면
        composable("personal_info") {
            PersonalInfoScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNextClick = {
                    navController.navigate("music_preference")
                }
            )
        }

        // 음악 취향 선택 화면
        composable("music_preference") {
            MusicPreferenceScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSkipClick = {
                    navController.navigate("signup_complete")
                },
                onNextClick = { selectedGenres ->
                    navController.navigate("signup_complete")
                }
            )
        }

        // 회원가입 완료 화면
        composable("signup_complete") {
            SignupCompleteScreen(
                onConfirmClick = {
                    navController.navigate("main") {
                        popUpTo("signup_complete") { inclusive = true }
                    }
                }
            )
        }

        // 메인 화면 (바텀 네비게이션이 있는 화면들)
        composable("main") {
            MainScreenWithBottomNav()
        }

        // 아이디 찾기 화면
        composable("findid") {
            Text(text = "준비 중인 기능입니다 - 아이디 찾기")
        }

        // 비밀번호 찾기 화면
        composable("findpassword") {
            Text(text = "준비 중인 기능입니다 - 비밀번호 찾기")
        }
    }
}

/**
 * 바텀 네비게이션이 포함된 메인 화면
 */
@Composable
fun MainScreenWithBottomNav() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"

    // StageDetailScreen인지 확인
    val shouldShowBottomBar = !currentRoute.startsWith("stage_detail")

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.ui.graphics.Color.White),
        containerColor = androidx.compose.ui.graphics.Color.White,
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomNavigationBar(
                    selectedItem = when (currentRoute) {
                        "home" -> NavigationItem.HOME
                        "stage" -> NavigationItem.STAGE
                        "map" -> NavigationItem.MAP
                        "mypage" -> NavigationItem.MYPAGE
                        else -> NavigationItem.HOME
                    },
                    onItemSelected = { navItem ->
                        val route = when (navItem) {
                            NavigationItem.HOME -> "home"
                            NavigationItem.STAGE -> "stage"
                            NavigationItem.MAP -> "map"
                            NavigationItem.MYPAGE -> "mypage"
                        }
                        navController.navigate(route) {
                            // 바텀 네비게이션 클릭 시 백스택 관리 - 상태 저장/복원
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(contentPadding)
        ) {
            // 홈 화면
            composable("home") {
                HomeScreen(
                    onSearchClick = { /* 검색 클릭 처리 */ },
                    onAlarmClick = { /* 알림 클릭 처리 */ }
                )
            }

            // 공연 화면
            composable("stage") {
                com.luckydut97.feature_stage.main.ui.StageScreen(
                    onPerformanceClick = { performanceId ->
                        navController.navigate("stage_detail/$performanceId")
                    }
                )
            }

            // 공연 상세 화면
            composable("stage_detail/{performanceId}") { backStackEntry ->
                val performanceId = backStackEntry.arguments?.getString("performanceId") ?: ""
                com.luckydut97.feature_stage.main.ui.StageDetailScreen(
                    performanceId = performanceId,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            // 지도 화면
            composable("map") {
                MapScreen()
            }

            // 마이페이지 화면 (임시)
            composable("mypage") {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(bottom = 108.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "마이페이지 화면입니다.",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
