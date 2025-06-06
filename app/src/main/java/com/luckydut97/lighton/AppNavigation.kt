package com.luckydut97.lighton

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

/**
 * 앱 전체의 메인 네비게이션을 처리하는 컴포넌트
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    isLoggedIn: Boolean = false
) {
    // 🔥 메인 화면으로 바로 가기 (홈화면 + 바텀 네비게이션)
    var startDestination by remember { mutableStateOf("main") }
    // 🔥 원래 코드: 테스트 완료 후 이걸 사용
    //var startDestination by remember { mutableStateOf(if (isLoggedIn) "main" else "auth") }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // 인증 관련 화면들 (로그인, 회원가입 등)
        composable("auth") {
            // 임시 스플래시 화면 - 나중에 실제 인증 플로우로 변경
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate("main") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }

        // 메인 화면 (바텀 네비게이션이 있는 화면들)
        composable("main") {
            MainScreenWithBottomNav()
        }

        // 기타 화면들
        composable("signup") {
            Text(text = "준비 중인 기능입니다 - 회원가입")
        }

        composable("findid") {
            Text(text = "준비 중인 기능입니다 - 아이디 찾기")
        }

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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
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
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier
        ) {
            // 홈 화면
            composable("home") {
                HomeScreen(
                    selectedNavItem = NavigationItem.HOME,
                    onSearchClick = { /* 검색 클릭 처리 */ },
                    onAlarmClick = { /* 알림 클릭 처리 */ }
                )
            }

            // 공연 화면 (임시)
            composable("stage") {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(bottom = 108.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "공연 화면입니다.",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // 지도 화면
            composable("map") {
                MapScreen(
                    selectedNavItem = NavigationItem.MAP
                )
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