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
import com.luckydut97.lighton.feature_mypage.main.ui.MyPageScreen

/**
 * 앱 전체의 메인 네비게이션을 처리하는 컴포넌트
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    isLoggedIn: Boolean = false
) {
    // 음악 취향 선택 화면 개발용 변수 - true로 설정하면 스플래시 후 화면으로 이동
    val isDevelopmentMode = true

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
                    if (isDevelopmentMode) {
                        // 음악 취향 선택 화면으로 이동(개발 모드)
                        navController.navigate("music_preference") {
                            popUpTo("splash") { inclusive = true }
                        }
                    } else {
                        // 로그인 화면으로 이동(일반 개발)
                        navController.navigate("login") {
                            popUpTo("splash") { inclusive = true }
                        }
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

        // 메인 화면 (바텀 네비게이션 포함)
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
                    selectedItem = when {
                        currentRoute == "home" -> NavigationItem.HOME
                        currentRoute.startsWith("stage") -> NavigationItem.STAGE
                        currentRoute == "map" -> NavigationItem.MAP
                        currentRoute == "mypage" -> NavigationItem.MYPAGE
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
                            popUpTo("home") {
                                saveState = route != "home"
                            }
                            launchSingleTop = true
                            restoreState = route != "home"
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
                    onAlarmClick = { /* 알림 클릭 처리 */ },
                    onRecommendedClick = {
                        navController.navigate("stage/recommended")
                    },
                    onPopularClick = {
                        navController.navigate("stage/popular")
                    }
                )
            }

            // 공연 화면
            composable("stage") {
                com.luckydut97.feature_stage.main.ui.StageScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onPerformanceClick = { performanceId ->
                        navController.navigate("stage_detail/$performanceId")
                    }
                )
            }

            // 특정 탭이 선택된 공연 화면
            composable("stage/{selectedTab}") { backStackEntry ->
                val selectedTab = backStackEntry.arguments?.getString("selectedTab") ?: "popular"
                com.luckydut97.feature_stage.main.ui.StageScreen(
                    initialTab = selectedTab,
                    onBackClick = {
                        navController.popBackStack()
                    },
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

            // 마이페이지 화면
            composable("mypage") {
                com.luckydut97.lighton.feature_mypage.main.ui.MyPageScreen(
                    onBackClick = { /* 뒤로가기 처리 */ },
                    onSettingClick = { /* 설정 클릭 처리 */ },
                    onActivityClick = { /* 내 활동 내역 처리 */ },
                    onRegisterClick = { /* 공연 등록 처리 */ },
                    onNoticeClick = { /* 공지사항 처리 */ },
                    onAppSettingClick = { /* 앱 설정 처리 */ },
                    onFaqClick = { /* FAQ 처리 */ },
                    onArtistApplyClick = { /* 아티스트 신청 처리 */ },
                    onTermsClick = { /* 이용약관 처리 */ },
                    onVersionClick = { /* 버전 정보 처리 */ },
                    onLogoutClick = { /* 로그아웃 처리 */ },
                    onWithdrawClick = { /* 회원 탈퇴 처리 */ }
                )
            }
        }
    }
}
