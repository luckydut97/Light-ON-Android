package com.luckydut97.feature_home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.lighton.feature_home.main.viewmodel.RecommendedPerformance

@Composable
fun RecommendedPerformanceItem(
    performance: RecommendedPerformance,
    onClick: (RecommendedPerformance) -> Unit = {},
    itemWidth: Dp = 130.dp,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // 이미지 크기 고정
    val imageSize = 124.dp

    Column(
        modifier = modifier
            .width(imageSize)
            .height(186.dp)
            .clickable { onClick(performance) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 원형 이미지 (고정 크기)
        Image(
            painter = painterResource(id = performance.imageResId),
            contentDescription = performance.title,
            modifier = Modifier
                .size(imageSize)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 텍스트 공간: width = 이미지 크기와 동일, height = 2줄(lineHeight × 2)
        Box(
            modifier = Modifier
                .width(imageSize)
                .height(42.dp) // lineHeight 21.sp x 2 (필요시 조절)
        ) {
            Text(
                text = performance.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                lineHeight = 21.sp,
                letterSpacing = (-0.16).sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Left,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
