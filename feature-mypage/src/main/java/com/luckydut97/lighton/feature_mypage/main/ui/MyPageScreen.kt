package com.luckydut97.lighton.feature_mypage.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.lighton.core.ui.components.CommonTopBar
import com.luckydut97.lighton.core.ui.components.LightonButton
import com.luckydut97.lighton.core.ui.components.LightonOutlinedButton
import com.luckydut97.lighton.core.ui.components.dialog.ArtistRegistrationDialog
import com.luckydut97.lighton.core.ui.theme.BrandColor
import com.luckydut97.lighton.core.ui.theme.PretendardFamily
import com.luckydut97.lighton.feature_mypage.component.MenuDivider
import com.luckydut97.lighton.feature_mypage.component.MenuItemButton
import com.luckydut97.lighton.feature_mypage.component.ProfileSection
import com.luckydut97.lighton.feature_mypage.model.UserProfile
import android.util.Log

enum class MemberType {
    REGULAR,    // 일반 회원
    ARTIST      // 아티스트 회원
}

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
    onWithdrawClick: () -> Unit = {},
    onNormalStageRegisterClick: () -> Unit = {},
    onBuskingRegisterClick: () -> Unit = {},
    onArtistRegisterClick: () -> Unit = {},
    memberType: MemberType = MemberType.REGULAR,
    isLoggedIn: Boolean = false, // 로그인 상태 파라미터 추가
    onLoginClick: () -> Unit = {}, // 로그인 버튼 클릭 콜백 추가
    onSignUpClick: () -> Unit = {} // 회원가입 버튼 클릭 콜백 추가
) {
    val tag = "🔍 디버깅: MyPageScreen"

    // 로그인 상태 체크 로그
    LaunchedEffect(isLoggedIn) {
        Log.d(tag, "=== 마이페이지 로그인 상태 체크 ===")
        Log.d(tag, "isLoggedIn 파라미터: $isLoggedIn")
        Log.d(tag, "분기 결과: ${if (isLoggedIn) "로그인된 사용자 UI" else "비로그인 사용자 UI"}")
    }

    // 아티스트 등록 다이얼로그 상태
    var showArtistRegistrationDialog by remember { mutableStateOf(false) }

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

        if (!isLoggedIn) {
            Log.d(tag, "🚫 비로그인 상태 - 로그인/회원가입 UI 표시")

            // 비로그인 시 UI
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White) // 전체 배경은 흰색
            ) {
                // 상단 박스 (F5F0FF 배경)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F0FF))
                        .padding(horizontal = 18.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    // 로그인 안내 텍스트
                    Text(
                        text = "로그인 상태가 아닙니다.",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = PretendardFamily,
                        color = Color.Black,
                        textAlign = TextAlign.Start
                    )
                    
                    Spacer(modifier = Modifier.height(1.dp))
                    
                    Text(
                        text = "로그인/회원가입 후 이용해주세요.",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = PretendardFamily,
                        color = Color.Black,
                        textAlign = TextAlign.Start
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // 로그인/회원가입 버튼들
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 로그인 버튼 - LightonOutlinedButton 사용
                        LightonOutlinedButton(
                            text = "로그인",
                            onClick = {
                                Log.d(tag, "🔐 로그인 버튼 클릭")
                                onLoginClick()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        )
                        
                        // 회원가입 버튼
                        LightonButton(
                            text = "회원가입",
                            onClick = {
                                Log.d(tag, "📝 회원가입 버튼 클릭")
                                onSignUpClick()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            backgroundColor = BrandColor,
                            contentColor = Color.White
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(18.dp))
                
                // 기본 메뉴들 (로그인하지 않아도 접근 가능한 메뉴들)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp)
                ) {
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
                    
                    MenuItemButton(
                        text = "이용약관",
                        onClick = onTermsClick
                    )
                    
                    MenuItemButton(
                        text = "버전 정보",
                        onClick = onVersionClick
                    )

                    MenuDivider()
                }
            }
        } else {
            Log.d(tag, "✅ 로그인 상태 - 프로필 섹션 및 전체 메뉴 표시")

            // 스크롤 가능한 컨텐츠 (로그인 시)
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
                    onRegisterClick = onRegisterClick,
                    onNormalStageRegisterClick = {
                        Log.d(tag, "🎭 일반 공연 등록 클릭 - 회원 타입: $memberType")

                        // 회원 타입에 따른 분기 처리
                        when (memberType) {
                            MemberType.REGULAR -> {
                                Log.d(tag, "일반 회원 → 아티스트 등록 다이얼로그 표시")
                                // 일반 회원이면 아티스트 등록 다이얼로그 표시
                                showArtistRegistrationDialog = true
                            }

                            MemberType.ARTIST -> {
                                Log.d(tag, "아티스트 회원 → 일반 공연 등록 화면으로 이동")
                                // 아티스트 회원이면 바로 일반 공연 등록 화면으로
                                onNormalStageRegisterClick()
                            }
                        }
                    },
                    onBuskingRegisterClick = {
                        Log.d(tag, "🎪 버스킹 등록 클릭 - 모든 회원 가능")
                        // 버스킹은 모든 회원이 가능
                        onBuskingRegisterClick()
                    }
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
                    onClick = {
                        Log.d(tag, "🚪 로그아웃 버튼 클릭")
                        onLogoutClick()
                    }
                )

                MenuItemButton(
                    text = "회원 탈퇴",
                    onClick = onWithdrawClick
                )
            }
        }
    }

    // 아티스트 등록 다이얼로그
    if (showArtistRegistrationDialog) {
        ArtistRegistrationDialog(
            onDismiss = { showArtistRegistrationDialog = false },
            onConfirm = {
                showArtistRegistrationDialog = false
                // 아티스트 등록 화면으로 이동
                onArtistRegisterClick()
            }
        )
    }
}
