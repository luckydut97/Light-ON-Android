package com.luckydut97.lighton.feature_stage_register.component.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.lighton.core.ui.components.LightonButton
import com.luckydut97.lighton.core.ui.theme.*
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    title: String,
    selectedStartDate: LocalDate?,
    selectedEndDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    onConfirm: () -> Unit
) {
    var currentDisplayMonth by remember { mutableStateOf(YearMonth.now()) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = bottomSheetState,
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.White,
            contentColor = Color.Black,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(4.dp)
                        .background(
                            color = Color.Gray.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 24.dp)
            ) {
                // 28dp 여백 추가 (공연 시작일 선택 위)
                Spacer(modifier = Modifier.height(28.dp))

                // 제목
                Text(
                    text = title,
                    fontFamily = PretendardFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = BrandColor,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                // 14dp 여백 (제목과 월/년도 사이)
                Spacer(modifier = Modifier.height(14.dp))

                // 월/년도 네비게이션
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = com.luckydut97.lighton.feature_stage_register.R.drawable.ic_left_arrow),
                        contentDescription = "이전 달",
                        tint = ClickableColor,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                currentDisplayMonth = currentDisplayMonth.minusMonths(1)
                            }
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${currentDisplayMonth.year}년 ${currentDisplayMonth.monthValue}월",
                            fontFamily = PretendardFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Icon(
                            painter = painterResource(id = com.luckydut97.lighton.feature_stage_register.R.drawable.ic_below_arrow),
                            contentDescription = "월 선택",
                            tint = ClickableColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Icon(
                        painter = painterResource(id = com.luckydut97.lighton.feature_stage_register.R.drawable.ic_right_arrow),
                        contentDescription = "다음 달",
                        tint = ClickableColor,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                currentDisplayMonth = currentDisplayMonth.plusMonths(1)
                            }
                    )
                }

                // 14dp 여백 (월/년도와 달력 사이)
                Spacer(modifier = Modifier.height(14.dp))

                // 달력 그리드
                CalendarGrid(
                    currentMonth = currentDisplayMonth,
                    selectedStartDate = selectedStartDate,
                    selectedEndDate = selectedEndDate,
                    onDateSelected = onDateSelected
                )

                Spacer(modifier = Modifier.height(32.dp))

                // 확인 버튼
                LightonButton(
                    text = "확인",
                    onClick = onConfirm,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}