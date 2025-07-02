package com.luckydut97.lighton.feature_mypage.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.lighton.core.ui.components.LightonButton
import com.luckydut97.lighton.core.ui.components.LightonOutlinedButton
import com.luckydut97.lighton.core.ui.theme.BrandColor
import com.luckydut97.lighton.core.ui.theme.PretendardFamily
import com.luckydut97.lighton.feature.mypage.R
import com.luckydut97.lighton.feature_mypage.model.UserProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSection(
    userProfile: UserProfile,
    onSettingClick: () -> Unit,
    onActivityClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onNormalStageRegisterClick: () -> Unit = {},
    onBuskingRegisterClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val profileBackground = Color(0xFFF5F0FF)
    val assistive = Color(0xFFC4C4C4)
    val loginIdColor = Color(0xFFA9A9A9)
    val clickableColor = Color(0xFFABABAB)

    var isRegisterSheetVisible by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(173.dp)
            .background(profileBackground)
            .padding(horizontal = 18.dp, vertical = 20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            // 프로필 이미지와 정보 Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 프로필 이미지
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .border(1.dp, assistive, CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_test_profile_img),
                        contentDescription = "Profile Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // 사용자 정보
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // 인사 메시지
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = BrandColor,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append(userProfile.username)
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Black,
                                    fontWeight = FontWeight.Normal
                                )
                            ) {
                                append("님 반갑습니다.")
                            }
                        },
                        fontSize = 18.sp,
                        fontFamily = PretendardFamily
                    )

                    Spacer(modifier = Modifier.height(1.dp))

                    // 로그인 ID
                    Text(
                        text = userProfile.loginId,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = PretendardFamily,
                        color = loginIdColor
                    )
                }

                // 설정 아이콘
                Icon(
                    painter = painterResource(id = R.drawable.ic_setting),
                    contentDescription = "Settings",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onSettingClick() },
                    tint = clickableColor
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 버튼 영역
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LightonOutlinedButton(
                    text = "내 활동 내역",
                    onClick = onActivityClick,
                    modifier = Modifier.weight(1f)
                )

                LightonButton(
                    text = "공연 등록",
                    onClick = {
                        onRegisterClick()
                        isRegisterSheetVisible = true
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    // BottomSheet for Register Stage Type Selection
    if (isRegisterSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { isRegisterSheetVisible = false },
            sheetState = sheetState,
            dragHandle = null,
            containerColor = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            RegisterStageTypeSheetContent(
                onNormalStageClick = {
                    isRegisterSheetVisible = false
                    onNormalStageRegisterClick()
                },
                onBuskingClick = {
                    isRegisterSheetVisible = false
                    onBuskingRegisterClick()
                },
                modifier = Modifier.padding(bottom = 28.dp)
            )
        }
    }
}