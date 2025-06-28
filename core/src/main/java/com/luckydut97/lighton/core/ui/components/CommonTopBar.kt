package com.luckydut97.lighton.core.ui.components

import androidx.compose.foundation.background
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
import com.luckydut97.lighton.core.R
import com.luckydut97.lighton.core.ui.theme.PretendardFamily

/**
 * 공통 상단바 컴포넌트
 *
 * @param title 중앙에 표시할 타이틀 텍스트
 * @param onBackClick 뒤로가기 버튼 클릭 콜백
 * @param modifier Modifier
 */
@Composable
fun CommonTopBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.White)
    ) {
        // 뒤로가기 버튼 - 좌측 18dp 여백
        LightonBackButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 18.dp)
        )

        // 중앙 타이틀 텍스트
        Text(
            text = title,
            fontFamily = PretendardFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
