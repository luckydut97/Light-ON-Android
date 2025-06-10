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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.lighton.feature_home.main.viewmodel.RecommendedPerformance

@Composable
fun RecommendedPerformanceItem(
    performance: RecommendedPerformance,
    onClick: (RecommendedPerformance) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(130.dp)
            .height(186.dp)
            .clickable { onClick(performance) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 원형 이미지 (130x130)
        Image(
            painter = painterResource(id = performance.imageResId),
            contentDescription = performance.title,
            modifier = Modifier
                .size(130.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 텍스트 (Hug 옵션, 최대 2줄)
        Text(
            text = performance.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            lineHeight = 21.sp,
            letterSpacing = (-0.16).sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(110.dp)
        )
    }
}
