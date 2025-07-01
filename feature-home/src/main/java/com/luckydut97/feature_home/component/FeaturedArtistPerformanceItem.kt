package com.luckydut97.feature_home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.lighton.core.ui.theme.DisabledColor
import com.luckydut97.lighton.feature_home.main.viewmodel.ArtistPerformance
import com.luckydut97.lighton.feature.home.R

@Composable
fun FeaturedArtistPerformanceItem(
    artistPerformance: ArtistPerformance,
    modifier: Modifier = Modifier,
    onClick: (ArtistPerformance) -> Unit = {}
) {
    Box(
        modifier = modifier
            .size(width = 330.dp, height = 94.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(Color.White) // 배경은 흰색으로
            .border(1.dp, Color(0xFFE9E9E9), RoundedCornerShape(5.dp)) // stroke만 회색
            .clickable { onClick(artistPerformance) }
            .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Left Image (feat_test_img로 고정)
            Image(
                painter = painterResource(id = R.drawable.feat_test_img),
                contentDescription = artistPerformance.performanceTitle,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(4.dp)),
                alignment = Alignment.Center
            )
            Spacer(modifier = Modifier.width(10.dp))

            // 텍스트 컬럼 계층: 윗 그룹/아랫 그룹 Column으로 그룹화
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                // 윗 그룹 (아티스트명, 공연명/장르)
                Column {
                    Text(
                        text = artistPerformance.artistName,
                        fontSize = 12.sp,
                        color = Color(0xFF555555),
                        fontWeight = FontWeight.Normal,
                        lineHeight = 3.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = artistPerformance.performanceTitle,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color(0xFFF5F0FF),
                                    shape = RoundedCornerShape(2.dp)
                                )
                                .height(19.dp)
                                .padding(horizontal = 4.dp, vertical = 0.dp),
                            contentAlignment = Alignment.Center // 이 부분을 추가!
                        ) {
                            Text(
                                text = artistPerformance.genre,
                                fontWeight = FontWeight.Bold,
                                fontSize = 8.sp,
                                lineHeight = 19.sp,
                                color = Color(0xFF8741FF)
                            )
                        }
                    }
                }
                // 아랫 그룹 (일정 Row, 위치 Row)
                Column(
                    verticalArrangement = Arrangement.spacedBy(-6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_clock_caption),
                            contentDescription = "clock",
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = artistPerformance.date,
                            fontSize = 12.sp,
                            color = Color(0xFF555555)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(12.dp)
                                .background(DisabledColor)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = artistPerformance.time,
                            fontSize = 12.sp,
                            color = Color(0xFF555555)
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_location_caption),
                            contentDescription = "location",
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = artistPerformance.place,
                            fontSize = 12.sp,
                            color = Color(0xFF555555)
                        )
                    }
                }
            }
        }
    }
}
