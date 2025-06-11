package com.luckydut97.feature_home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.feature_home.component.RecommendedPerformanceItem
import com.luckydut97.lighton.feature_home.main.viewmodel.RecommendedPerformance

@Composable
fun RecommendationSection(
    performances: List<RecommendedPerformance>,
    onMoreClick: () -> Unit = {},
    onPerformanceClick: (RecommendedPerformance) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // 화면 크기에 따른 동적 값들
    val horizontalPadding = (screenWidth * 0.043f).coerceAtLeast(16.dp) // 약 18dp for 414dp screen
    val itemSpacing = (screenWidth * 0.039f).coerceAtLeast(12.dp) // 약 16dp for 414dp screen
    val itemWidth = (screenWidth * 0.314f).coerceAtLeast(110.dp) // 약 130dp for 414dp screen

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp)
            .background(Color.White)
    ) {
        Column {
            // Row1: 제목과 화살표 (70dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(27.dp)
                    ) {
                        // 좌측 "추천 공연" 텍스트
                        Text(
                            text = "추천 공연",
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = horizontalPadding)
                        )

                        // 우측 화살표 아이콘
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "더보기",
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = horizontalPadding)
                                .size(29.dp)
                                .clickable { onMoreClick() },
                            tint = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Row2: 가로 스크롤 아이템들 (166dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(166.dp),
                contentAlignment = Alignment.Center
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(166.dp), // 166dp - 상하 10dp 패딩
                    horizontalArrangement = Arrangement.spacedBy(itemSpacing),
                    contentPadding = PaddingValues(horizontal = horizontalPadding)
                ) {
                    items(performances) { performance ->
                        RecommendedPerformanceItem(
                            performance = performance,
                            onClick = onPerformanceClick,
                            itemWidth = itemWidth
                        )
                    }
                }
            }
        }
    }
}
