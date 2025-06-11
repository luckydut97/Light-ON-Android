package com.luckydut97.feature_home.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeroImageSection(
    modifier: Modifier = Modifier
) {
    val pages = listOf(
        Pair("메인 텍스트는 최대 2줄까지 허용합니다. 추천음악 1", "서브텍스트 최대 1줄 허용 1"),
        Pair("메인 텍스트는 최대 2줄까지 허용합니다. 추천음악 2", "서브텍스트 최대 1줄 허용 2"),
        Pair("메인 텍스트는 최대 2줄까지 허용합니다. 추천음악 3", "서브텍스트 최대 1줄 허용 3")
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })

    // 5초마다 자동으로 페이지 변경
    LaunchedEffect(pagerState) {
        while (true) {
            delay(5000)
            val nextPage = (pagerState.currentPage + 1) % pages.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Box(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(Color(0xFFC5C5C5))
            ) {
                // 그라데이션 오버레이 (하단에만 적용)
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0x00000000), // 위(투명)
                                    Color(0x66000000), // 아래(블랙, 40% 투명)
                                ),
                                startY = 350f,
                                endY = Float.POSITIVE_INFINITY
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .size(width = 402.dp, height = 167.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 310.dp, height = 72.dp)
                    ) {
                        Text(
                            text = pages[page].first,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            lineHeight = 36.sp,
                            letterSpacing = (-1).sp,
                            maxLines = 2,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .size(width = 310.dp, height = 27.dp)
                    ) {
                        Text(
                            text = pages[page].second,
                            fontSize = 18.sp,
                            color = Color.White,
                            lineHeight = 36.sp,
                            letterSpacing = (-1).sp,
                            maxLines = 1,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    // 인디케이터를 위한 공간만 확보 (실제 인디케이터는 밖에서 렌더링)
                    Box(modifier = Modifier.height(18.dp))
                }
            }
        }

        // 인디케이터를 사진 위에 오버레이 (원래 위치 그대로, 하지만 HorizontalPager 밖에서 렌더링)
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(width = 402.dp, height = 167.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 텍스트 영역을 위한 공간 (72dp + 8dp + 27dp + 30dp = 137dp)
            Spacer(modifier = Modifier.height(137.dp))

            // 인디케이터 (원래 위치 그대로)
            Box(
                modifier = Modifier
                    .width(46.dp)
                    .height(18.dp)
                    .background(color = Color(0x33000000), shape = RoundedCornerShape(100.dp)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(pages.size) { index ->
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(
                                    color = if (index == pagerState.currentPage) Color.White else Color.White.copy(
                                        alpha = 0.3f
                                    ),
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }
        }
    }
}
