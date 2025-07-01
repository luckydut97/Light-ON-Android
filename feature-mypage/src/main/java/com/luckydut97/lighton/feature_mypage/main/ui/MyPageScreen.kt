package com.luckydut97.lighton.feature_mypage.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.luckydut97.lighton.core.ui.components.CommonTopBar
import com.luckydut97.lighton.feature_mypage.component.MenuDivider
import com.luckydut97.lighton.feature_mypage.component.MenuItemButton
import com.luckydut97.lighton.feature_mypage.component.ProfileSection
import com.luckydut97.lighton.feature_mypage.model.UserProfile

@Composable
fun MyPageScreen(
    onBackClick: () -> Unit = {},
    onSettingClick: () -> Unit = {},
    onActivityClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onNoticeClick: () -> Unit = {},
    onAppSettingClick: () -> Unit = {},
    onFaqClick: () -> Unit = {},
    onArtistApplyClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onVersionClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onWithdrawClick: () -> Unit = {}
) {
    // 더미 사용자 데이터
    val userProfile = remember {
        UserProfile(
            username = "프로그라피",
            loginId = "Prography5307",
            profileImageUrl = null
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 상단바
        CommonTopBar(
            title = "마이페이지",
            onBackClick = onBackClick
        )

        // 스크롤 가능한 컨텐츠
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // 프로필 섹션
            ProfileSection(
                userProfile = userProfile,
                onSettingClick = onSettingClick,
                onActivityClick = onActivityClick,
                onRegisterClick = onRegisterClick
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 메뉴 섹션
            // 첫 번째 그룹: 공지사항, 앱 설정, FAQ
            MenuItemButton(
                text = "공지사항",
                onClick = onNoticeClick
            )

            MenuItemButton(
                text = "앱 설정",
                onClick = onAppSettingClick
            )

            MenuItemButton(
                text = "FAQ",
                onClick = onFaqClick
            )

            MenuDivider()

            // 두 번째 그룹: 아티스트 신청
            MenuItemButton(
                text = "아티스트 신청",
                onClick = onArtistApplyClick
            )

            MenuDivider()

            // 세 번째 그룹: 이용약관, 버전 정보
            MenuItemButton(
                text = "이용약관",
                onClick = onTermsClick
            )

            MenuItemButton(
                text = "버전 정보",
                onClick = onVersionClick
            )

            MenuDivider()

            // 네 번째 그룹: 로그아웃, 회원 탈퇴
            MenuItemButton(
                text = "로그아웃",
                onClick = onLogoutClick
            )

            MenuItemButton(
                text = "회원 탈퇴",
                onClick = onWithdrawClick
            )
        }
    }
}
