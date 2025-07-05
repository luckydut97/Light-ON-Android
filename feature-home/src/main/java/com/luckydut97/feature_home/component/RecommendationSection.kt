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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.feature_home.component.RecommendedPerformanceItem
import com.luckydut97.lighton.core.ui.theme.AssistiveColor
import com.luckydut97.lighton.feature.home.R
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


    val horizontalPadding = 18.dp
    val itemSpacing = 14.dp
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
                            .clickable { onMoreClick() }  // 전체 Box를 클릭 가능하게
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
                            painter = painterResource(id = R.drawable.ic_right_arrow),
                            contentDescription = "더보기",
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = horizontalPadding)
                                .size(15.dp),
                            tint = AssistiveColor
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Row2: 가로 스크롤 아이템들 (186dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(186.dp),
                contentAlignment = Alignment.Center
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(186.dp), // 186dp - 상하 10dp 패딩
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
