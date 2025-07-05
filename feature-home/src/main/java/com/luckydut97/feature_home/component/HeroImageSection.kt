package com.luckydut97.feature_home.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.luckydut97.lighton.feature.home.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeroImageSection(
    modifier: Modifier = Modifier
) {
    val pages = listOf(
        Pair("요즘 가장 핫한 아티스트 FiveDays 썸머 페스티벌", "파이브데이즈 공연 신청하기"),
        Pair("[잠실] 한강 공원 Light ON 홀리데이 버스킹", "잠실동 프로그라피 2F"),
        Pair("[여의도] 한강 공원 내 손안의 서울 페스티벌", "여의도동 프로그라피 3F")
    )

    val imageResources = listOf(
        R.drawable.main_test_img,
        R.drawable.main_test_img3,
        R.drawable.main_test_img2
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
            val isOneLine = remember(pages[page].first) {
                !pages[page].first.contains("\n") && pages[page].first.length <= 18 // rough single line
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                Image(
                    painter = painterResource(id = imageResources[page]),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
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
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .size(width = 402.dp, height = 167.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.Start
                    ) {
                        if (isOneLine) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp)
                                .height(if (isOneLine) 36.dp else 72.dp)
                        ) {
                            Text(
                                text = pages[page].first,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                lineHeight = 36.sp,
                                letterSpacing = (-1).sp,
                                maxLines = 2,
                                textAlign = TextAlign.Start,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp)
                                .height(27.dp)
                        ) {
                            Text(
                                text = pages[page].second,
                                fontSize = 18.sp,
                                color = Color.White,
                                lineHeight = 36.sp,
                                letterSpacing = (-1).sp,
                                maxLines = 1,
                                textAlign = TextAlign.Start,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                        Box(modifier = Modifier.height(18.dp))
                    }
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
