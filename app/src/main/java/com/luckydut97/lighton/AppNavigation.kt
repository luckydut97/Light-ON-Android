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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
 * 간단한 전역 사용자 상태 관리
 * - isArtistMember: 아티스트 회원 여부
 * - isLoggedIn: 로그인 상태
 * - accessToken: 액세스 토큰
 * - userId: 사용자 ID
 * - userName: 사용자 이름
 *
 * - hasValidToken(): 토큰 유효성 확인
 * - login(): 로그인 및 상태 저장
 * - logout(): 로그아웃 및 상태 초기화
 */
object UserState {
    var isArtistMember: Boolean = false // 테스트를 위해 일반회원으로 초기 설정
    var isLoggedIn: Boolean = false // 로그인 상태
    var accessToken: String? = null // 액세스 토큰
    var userId: String? = null // 사용자 ID
    var userName: String? = null // 사용자 이름

    // 편의 메서드
    fun hasValidToken(): Boolean = !accessToken.isNullOrEmpty()

    fun login(token: String, userId: String, userName: String, isArtist: Boolean = false) {
        this.accessToken = token
        this.userId = userId
        this.userName = userName
        this.isArtistMember = isArtist
        this.isLoggedIn = true
    }

    fun logout() {
        this.accessToken = null
        this.userId = null
        this.userName = null
        this.isArtistMember = false
        this.isLoggedIn = false
    }
}

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
    val showMainScreen = false    // 메인 화면으로 바로 이동
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
                onNavigateToMain = {
                    // 무조건 메인 화면으로 이동
                    navController.navigate("main") {
                        popUpTo("splash") { inclusive = true }
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
                    // 로그인 성공 시 UserState 업데이트 (임시)
                    UserState.login(
                        token = "temp_access_token",
                        userId = "temp_user_id",
                        userName = "테스트 사용자",
                        isArtist = false
                    )

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
                    // 테스트용: 회원가입 완료 시 자동 로그인 상태로 변경
                    UserState.login(
                        token = "signup_complete_token",
                        userId = "signup_user_id",
                        userName = "신규 회원",
                        isArtist = false
                    )
                    
                    navController.navigate("main") {
                        popUpTo("signup_complete") { inclusive = true }
                    }
                }
            )
        }

        // 메인 화면 (바텀 네비게이션 포함)
        composable("main") {
            MainScreenWithBottomNav(
                onNavigateToRegister = { route, updateMemberType ->
                    navController.navigate(route)
                    if (updateMemberType) {
                        UserState.isArtistMember = true
                    }
                },
                onNavigateToLogin = {
                    navController.navigate("login")
                },
                onNavigateToSignUp = {
                    navController.navigate("signup")
                },
                memberType = if (UserState.isArtistMember) com.luckydut97.lighton.feature_mypage.main.ui.MemberType.ARTIST else com.luckydut97.lighton.feature_mypage.main.ui.MemberType.REGULAR
            )
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
                    // 일반공연 등록 완료 후 메인 화면으로 이동
                    navController.navigate("main") {
                        popUpTo("normal_stage_register") { inclusive = true }
                    }
                },
                onArtistRegisterClick = {
                    // 아티스트 등록 화면으로 이동
                    navController.navigate("artist_register") {
                        popUpTo("normal_stage_register") { inclusive = true }
                    }
                },
                isArtistMember = UserState.isArtistMember
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
                    // 버스킹 공연 등록 완료 후 메인 화면으로 이동
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
                    // 아티스트 등록 완료 후 회원 타입을 아티스트로 변경
                    UserState.isArtistMember = true
                    // 아티스트 등록 완료 후 메인 화면으로 이동
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
fun MainScreenWithBottomNav(
    onNavigateToRegister: (String, Boolean) -> Unit = { _, _ -> },
    onNavigateToLogin: () -> Unit = {}, // 로그인 화면으로 이동하는 콜백 추가
    onNavigateToSignUp: () -> Unit = {}, // 회원가입 화면으로 이동하는 콜백 추가
    memberType: com.luckydut97.lighton.feature_mypage.main.ui.MemberType = com.luckydut97.lighton.feature_mypage.main.ui.MemberType.REGULAR
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"

    // StageDetailScreen인지 확인
    val shouldShowBottomBar = !currentRoute.startsWith("stage_detail")

    // 등록 화면으로 이동하는 콜백 확장 (회원 타입 업데이트 포함)
    val handleNavigateToRegister: (String, Boolean) -> Unit = { route, updateMemberType ->
        onNavigateToRegister(route, updateMemberType)
    }

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
            modifier = Modifier.padding(contentPadding),
            enterTransition = {
                // 별도: stage_detail 화면만 오른쪽에서 슬라이드 진입
                val targetRoute = targetState.destination.route ?: ""
                val initialRoute = initialState?.destination?.route ?: ""
                if (targetRoute.startsWith("stage_detail")) {
                    // 오른쪽에서 왼쪽으로 슬라이드 진입
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(350)
                    )
                } else if (initialRoute.startsWith("stage_detail")) {
                    // stage_detail에서 나올 때는 왼쪽에서 오른쪽으로 슬라이드
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> -fullWidth },
                        animationSpec = tween(350)
                    )
                } else {
                    val targetOrder = getNavigationOrder(targetRoute)
                    val initialOrder = getNavigationOrder(initialRoute)
                    if (targetOrder > initialOrder) {
                        slideInHorizontally(
                            initialOffsetX = { fullWidth -> fullWidth },
                            animationSpec = tween(300)
                        )
                    } else {
                        slideInHorizontally(
                            initialOffsetX = { fullWidth -> -fullWidth },
                            animationSpec = tween(300)
                        )
                    }
                }
            },
            exitTransition = {
                // 별도: stage_detail 화면만 왼쪽으로 슬라이드 나가기
                val targetRoute = targetState.destination.route ?: ""
                val initialRoute = initialState?.destination?.route ?: ""
                if (initialRoute.startsWith("stage_detail")) {
                    // 왼쪽에서 오른쪽으로 슬라이드 나가기
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(350)
                    )
                } else if (targetRoute.startsWith("stage_detail")) {
                    // stage_detail로 진입할 때는 오른쪽에서 왼쪽으로 슬라이드
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> -fullWidth },
                        animationSpec = tween(350)
                    )
                } else {
                    val targetOrder = getNavigationOrder(targetRoute)
                    val initialOrder = getNavigationOrder(initialRoute)
                    if (targetOrder > initialOrder) {
                        slideOutHorizontally(
                            targetOffsetX = { fullWidth -> -fullWidth },
                            animationSpec = tween(300)
                        )
                    } else {
                        slideOutHorizontally(
                            targetOffsetX = { fullWidth -> fullWidth },
                            animationSpec = tween(300)
                        )
                    }
                }
            }
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
                val selectedTab =
                    backStackEntry.arguments?.getString("selectedTab") ?: "popular"
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
                    },
                    onLoginClick = {
                        // 로그인 화면으로 이동
                        onNavigateToLogin()
                    },
                    onSignUpClick = {
                        // 회원가입 화면으로 이동 
                        onNavigateToSignUp()
                    },
                    isLoggedIn = UserState.isLoggedIn
                )
            }

            // 지도 화면
            composable("map") {
                MapScreen()
            }

            // 마이페이지 화면 - 등록 화면들은 콜백으로 처리
            composable("mypage") {
                // 로그인 체크 다이얼로그 상태
                var showLoginDialog by remember { mutableStateOf(false) }

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
                    onLogoutClick = {
                        // 로그아웃 처리
                        UserState.logout()
                    },
                    onWithdrawClick = { /* 회원 탈퇴 처리 */ },
                    memberType = memberType,
                    isLoggedIn = UserState.isLoggedIn, // 로그인 상태 전달
                    onLoginClick = {
                        // 로그인 화면으로 이동
                        onNavigateToLogin()
                    },
                    onSignUpClick = {
                        // 회원가입 화면으로 이동
                        onNavigateToSignUp()
                    },
                    onNormalStageRegisterClick = {
                        // 로그인 체크 먼저 수행
                        if (!UserState.isLoggedIn) {
                            showLoginDialog = true
                            return@MyPageScreen
                        }

                        // 일반 공연 등록 - 아티스트 회원만 가능
                        when (memberType) {
                            com.luckydut97.lighton.feature_mypage.main.ui.MemberType.ARTIST -> {
                                // 메인 NavController로 이동
                                handleNavigateToRegister("normal_stage_register", false)
                            }

                            else -> {
                                // 일반 회원이면 MyPageScreen에서 다이얼로그 처리
                            }
                        }
                    },
                    onBuskingRegisterClick = {
                        // 로그인 체크 먼저 수행
                        if (!UserState.isLoggedIn) {
                            showLoginDialog = true
                            return@MyPageScreen
                        }

                        // 버스킹 공연 등록 - 모든 회원 가능
                        handleNavigateToRegister("busking_stage_register", false)
                    },
                    onArtistRegisterClick = {
                        // 로그인 체크 먼저 수행
                        if (!UserState.isLoggedIn) {
                            showLoginDialog = true
                            return@MyPageScreen
                        }

                        // 아티스트 등록 화면으로 이동. 완료 후 회원 타입을 전달받은 콜백으로 변경
                        handleNavigateToRegister("artist_register", true)
                    }
                )

                // 로그인 체크 다이얼로그
                if (showLoginDialog) {
                    com.luckydut97.lighton.core.ui.components.dialog.LoginRequiredDialog(
                        onDismiss = { showLoginDialog = false },
                        onLoginClick = {
                            showLoginDialog = false
                            onNavigateToLogin()
                        },
                        onSignUpClick = {
                            showLoginDialog = false
                            onNavigateToSignUp()
                        }
                    )
                }
            }
        }
    }
}

/**
 * 네비게이션 아이템별 순서를 반환하는 헬퍼 함수
 */
private fun getNavigationOrder(route: String): Int {
    return when (route) {
        "home" -> 0
        "stage" -> 1
        "map" -> 2
        "mypage" -> 3
        else -> 0
    }
}
