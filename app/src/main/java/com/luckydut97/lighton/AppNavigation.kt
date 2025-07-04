package com.luckydut97.lighton

import androidx.compose.foundation.background
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.luckydut97.lighton.feature_auth.splash.ui.SplashScreen
import com.luckydut97.lighton.feature_home.main.ui.HomeScreen
import com.luckydut97.lighton.core.ui.components.BottomNavigationBar
import com.luckydut97.lighton.core.ui.components.NavigationItem
import com.luckydut97.lighton.feature_auth.login.ui.EmailLoginScreen
import com.luckydut97.lighton.feature_auth.login.ui.SocialLoginWebView
import com.luckydut97.lighton.feature_auth.login.ui.SocialLoginResult
import com.luckydut97.lighton.feature_auth.signup.ui.SignUpScreen
import com.luckydut97.lighton.feature_auth.signup.ui.PersonalInfoScreen
import com.luckydut97.lighton.feature_auth.signup.ui.MusicPreferenceScreen
import com.luckydut97.lighton.feature_auth.signup.ui.SignupCompleteScreen
import com.luckydut97.lighton.feature_map.main.ui.MapScreen

/**
 * 앱 전체의 메인 네비게이션을 처리하는 컴포넌트
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    isLoggedIn: Boolean = false
) {
    // 🔧 개발용 변수들 - 원하는 화면으로 바로 이동
    //
    // 사용법: 원하는 화면의 변수를 true로 설정
    // ⚠️ 주의: 한 번에 하나의 변수만 true로 설정해야 함
    //
    // 예시:
    // val showLoginScreen = true     → 로그인 화면으로 바로 이동
    // val showSignupScreen = true    → 회원가입 화면으로 바로 이동
    // val showPersonalInfoScreen = true  → 개인정보 입력 화면으로 바로 이동
    //
    val isDevelopmentMode = false  // 음악 취향 선택 화면으로 바로 이동
    val showLoginScreen = false   // 로그인 화면으로 바로 이동
    val showSignupScreen = false   // 회원가입 화면으로 바로 이동
    val showPersonalInfoScreen = false  // 개인정보 입력 화면으로 바로 이동
    val showMusicPreferenceScreen = false  // 음악 취향 선택 화면으로 바로 이동
    val showMainScreen = true     // 메인 화면으로 바로 이동
    val showNormalStageRegisterScreen = false  // 일반공연 등록 화면으로 바로 이동
    val showBuskingStageRegisterScreen = false  // 버스킹 등록 화면으로 바로 이동
    val showArtistRegisterScreen = false  // 아티스트 등록 화면으로 바로 이동

    // 개발용 시작 화면 결정
    var startDestination by remember {
        mutableStateOf(
            when {
                showNormalStageRegisterScreen -> "normal_stage_register"
                showBuskingStageRegisterScreen -> "busking_stage_register"
                showArtistRegisterScreen -> "artist_register"
                showMainScreen -> "main"
                showMusicPreferenceScreen -> "music_preference"
                showPersonalInfoScreen -> "personal_info/999"
                showSignupScreen -> "signup"
                isDevelopmentMode -> "music_preference"
                isLoggedIn -> "main"
                else -> "splash"
            }
        )
    }

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
                    // 메인 화면으로 이동
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onKakaoLoginClick = {
                    // 카카오 소셜 로그인 시작
                    navController.navigate("social_login/kakao")
                },
                onGoogleLoginClick = {
                    // 구글 소셜 로그인 시작
                    navController.navigate("social_login/google")
                },
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

        // 소셜 로그인 WebView 화면
        composable("social_login/{provider}") { backStackEntry ->
            val provider = backStackEntry.arguments?.getString("provider") ?: "kakao"
            val title = when (provider) {
                "kakao" -> "카카오 로그인"
                "google" -> "구글 로그인"
                else -> "소셜 로그인"
            }

            SocialLoginWebView(
                title = title,
                authUrl = "https://api.lighton.com/oauth/$provider", // TODO: 실제 OAuth URL 동적 로딩
                callbackUrlPrefix = "https://api.lighton.com/oauth/$provider/callback",
                onResult = { result ->
                    when (result) {
                        is SocialLoginResult.Success -> {
                            // 인가 코드 받음 - TODO: 실제 콜백 API 호출
                            // 임시로 개인정보 입력 화면으로 이동
                            navController.navigate("personal_info/123") {
                                popUpTo("social_login/$provider") { inclusive = true }
                            }
                        }

                        is SocialLoginResult.Error -> {
                            // 에러 처리 - 로그인 화면으로 복귀
                            navController.popBackStack()
                        }

                        is SocialLoginResult.Cancelled -> {
                            // 사용자 취소 - 로그인 화면으로 복귀
                            navController.popBackStack()
                        }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // 회원가입 화면
        composable("signup") {
            SignUpScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNextClick = { temporaryUserId ->
                    navController.navigate("personal_info/$temporaryUserId")
                }
            )
        }

        // 개인정보 입력 화면 (일반 회원가입)
        composable("personal_info/{temporaryUserId}") { backStackEntry ->
            val temporaryUserId = backStackEntry.arguments?.getString("temporaryUserId")

            PersonalInfoScreen(
                temporaryUserId = temporaryUserId,
                onBackClick = {
                    navController.popBackStack()
                },
                onNextClick = {
                    navController.navigate("music_preference")
                },
                onCompleteClick = {
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

        // 일반 공연 등록 화면
        composable("normal_stage_register") {
            com.luckydut97.lighton.feature_stage_register.ui.NormalStageRegisterScreen(
                onBackClick = {
                    navController.navigate("main") {
                        popUpTo("normal_stage_register") { inclusive = true }
                    }
                },
                onRegisterClick = {
                    // TODO: 등록 완료 후 처리
                    navController.navigate("main") {
                        popUpTo("normal_stage_register") { inclusive = true }
                    }
                }
            )
        }

        // 버스킹 공연 등록 화면
        composable("busking_stage_register") {
            com.luckydut97.lighton.feature_stage_register.ui.BuskingStageRegisterScreen(
                onBackClick = {
                    navController.navigate("main") {
                        popUpTo("busking_stage_register") { inclusive = true }
                    }
                },
                onRegisterClick = {
                    // TODO: 등록 완료 후 처리
                    navController.navigate("main") {
                        popUpTo("busking_stage_register") { inclusive = true }
                    }
                }
            )
        }

        // 아티스트 등록 화면
        composable("artist_register") {
            com.luckydut97.lighton.feature_stage_register.ui.ArtistRegisterScreen(
                onBackClick = {
                    navController.navigate("main") {
                        popUpTo("artist_register") { inclusive = true }
                    }
                },
                onRegisterClick = {
                    // TODO: 등록 완료 후 처리
                    navController.navigate("main") {
                        popUpTo("artist_register") { inclusive = true }
                    }
                }
            )
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
        contentColor = androidx.compose.ui.graphics.Color.Black,
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
