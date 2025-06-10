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
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(289.dp)
            .background(Color.White)
    ) {
        Column {
            // Row1: 제목과 화살표 (63dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(63.dp)
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
                                .padding(start = 18.dp)
                        )

                        // 우측 화살표 아이콘
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "더보기",
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 18.dp)
                                .size(24.dp)
                                .clickable { onMoreClick() },
                            tint = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Row2: 가로 스크롤 아이템들 (206dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(206.dp),
                contentAlignment = Alignment.Center
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(196.dp), // 206dp - 상하 10dp 패딩
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 18.dp) // 좌우 18dp 패딩
                ) {
                    items(performances) { performance ->
                        RecommendedPerformanceItem(
                            performance = performance,
                            onClick = onPerformanceClick
                        )
                    }
                }
            }
        }
    }
}
