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
import com.luckydut97.feature_home.component.FeaturedArtistPerformanceItem
import com.luckydut97.lighton.feature_home.main.viewmodel.ArtistPerformance

@Composable
fun FeaturedArtistPerformanceSection(
    performances: List<ArtistPerformance>,
    onMoreClick: () -> Unit = {},
    onPerformanceClick: (ArtistPerformance) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val horizontalPadding = (screenWidth * 0.043f).coerceAtLeast(16.dp)
    val itemSpacing = (screenWidth * 0.039f).coerceAtLeast(12.dp)
    val itemWidth = 330.dp // 고정
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(170.dp) //마지막줄 정렬문제 임시해결
            .background(Color.White)
    ) {
        Column {
            // Title Row
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(37.dp)
                    .clickable { onMoreClick() }  // 전체 Box를 클릭 가능하게
            ) {
                Text(
                    text = "주목할 만한 아티스트 공연",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = horizontalPadding)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "더보기",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = horizontalPadding)
                        .size(24.dp),
                    tint = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(9.dp))
            // Item List Row
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(104.dp),
                contentAlignment = Alignment.Center
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(itemSpacing),
                    contentPadding = PaddingValues(horizontal = horizontalPadding)
                ) {
                    items(performances) { artistPerformance ->
                        FeaturedArtistPerformanceItem(
                            artistPerformance = artistPerformance,
                            onClick = onPerformanceClick
                        )
                    }
                }
            }
        }
    }
}
