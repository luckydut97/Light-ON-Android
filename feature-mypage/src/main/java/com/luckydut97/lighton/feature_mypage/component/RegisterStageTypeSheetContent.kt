package com.luckydut97.lighton.feature_mypage.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.lighton.core.ui.theme.AssistiveColor
import com.luckydut97.lighton.core.ui.theme.PretendardFamily
import com.luckydut97.lighton.feature.mypage.R

@Composable
fun RegisterStageTypeSheetContent(
    onNormalStageClick: () -> Unit,
    onBuskingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "어떤 공연을 등록할까요?",
            fontFamily = PretendardFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(28.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(108.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 첫 번째 줄 - 일반공연
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clickable { onNormalStageClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_normal_stage),
                        contentDescription = "일반공연",
                        modifier = Modifier.size(38.dp),
                        tint = Color.Unspecified
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "일반공연",
                            fontFamily = PretendardFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Black,
                            letterSpacing = (-1).sp
                        )
                        Text(
                            text = "아티스트 회원만 등록이 가능합니다.",
                            fontFamily = PretendardFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = Color.Black,
                            letterSpacing = (-1).sp
                        )
                    }

                    Icon(
                        painter = painterResource(id = R.drawable.ic_right_arrow),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = AssistiveColor
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 두 번째 줄 - 버스킹
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clickable { onBuskingClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_busking),
                        contentDescription = "버스킹",
                        modifier = Modifier.size(38.dp),
                        tint = Color.Unspecified
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "버스킹",
                            fontFamily = PretendardFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Black,
                            letterSpacing = (-1).sp
                        )
                        Text(
                            text = "일반 회원도 등록이 가능합니다.",
                            fontFamily = PretendardFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = Color.Black,
                            letterSpacing = (-1).sp
                        )
                    }

                    Icon(
                        painter = painterResource(id = R.drawable.ic_right_arrow),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = AssistiveColor
                    )
                }
            }
        }
    }
}