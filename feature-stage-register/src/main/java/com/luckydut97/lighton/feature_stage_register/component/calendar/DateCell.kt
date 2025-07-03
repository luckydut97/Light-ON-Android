package com.luckydut97.lighton.feature_stage_register.component.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.lighton.core.ui.theme.*
import java.time.LocalDate

@Composable
fun DateCell(
    date: LocalDate,
    isCurrentMonth: Boolean,
    isToday: Boolean,
    isSelectable: Boolean,
    isStartDate: Boolean,
    isEndDate: Boolean,
    isInRange: Boolean,
    onClick: () -> Unit
) {
    val textColor = when {
        !isCurrentMonth -> Color.Transparent
        !isSelectable -> AssistiveColor.copy(alpha = 0.3f)
        isStartDate -> Color.White
        isEndDate -> BrandColor
        isInRange -> CaptionColor
        else -> CaptionColor
    }

    val fontWeight = when {
        isStartDate || isEndDate -> FontWeight.Bold
        else -> FontWeight.Normal
    }

    Box(
        modifier = Modifier
            .size(47.dp)
            .clickable(enabled = isSelectable && isCurrentMonth) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // 범위 배경 - 연속된 배경 만들기 (잘림 방지)
        if (isInRange) {
            // 원과 똑같은 높이 35dp로 배경 설정
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(35.dp)
                    .background(
                        color = Color(0xFFF5F0FF),
                        shape = when {
                            isStartDate && isEndDate -> RoundedCornerShape(100.dp) // 시작일과 종료일이 같은 경우
                            isStartDate -> RoundedCornerShape(
                                topStart = 100.dp,
                                bottomStart = 100.dp,
                                topEnd = 0.dp,
                                bottomEnd = 0.dp
                            )

                            isEndDate -> RoundedCornerShape(
                                topStart = 0.dp,
                                bottomStart = 0.dp,
                                topEnd = 100.dp,
                                bottomEnd = 100.dp
                            )

                            else -> RoundedCornerShape(0.dp) // 중간 날짜들은 직사각형으로 완전 연결
                        }
                    )
            )
        }

        // 선택된 날짜 배경
        when {
            isStartDate -> {
                // 시작일: 35*35 BrandColor 원
                Box(
                    modifier = Modifier
                        .size(35.dp)
                        .background(
                            color = BrandColor,
                            shape = CircleShape
                        )
                )
            }
            isEndDate -> {
                // 종료일: 35*35 Stroke + 흰색 배경
                Box(
                    modifier = Modifier
                        .size(35.dp)
                        .border(
                            width = 2.dp,
                            color = BrandColor,
                            shape = CircleShape
                        )
                        .background(
                            color = Color.White,
                            shape = CircleShape
                        )
                )
            }
        }

        // 날짜 텍스트
        if (isCurrentMonth) {
            Text(
                text = date.dayOfMonth.toString(),
                fontFamily = PretendardFamily,
                fontWeight = fontWeight,
                fontSize = 18.sp,
                color = textColor
            )
        }
    }
}