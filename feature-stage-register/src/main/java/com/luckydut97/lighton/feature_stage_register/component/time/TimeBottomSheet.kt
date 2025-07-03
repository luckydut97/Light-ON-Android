package com.luckydut97.lighton.feature_stage_register.component.time

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.lighton.core.ui.components.LightonButton
import com.luckydut97.lighton.core.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    title: String,
    selectedTime: String,
    onTimeSelected: (String) -> Unit,
    onConfirm: () -> Unit
) {
    // 시간 파싱 (기본값: 오후 6:00)
    val (initialAmPm, initialHour, initialMinute) = remember(selectedTime) {
        if (selectedTime.isEmpty()) {
            Triple("오후", 6, 0)
        } else {
            parseTime(selectedTime)
        }
    }

    var selectedAmPm by remember { mutableStateOf(initialAmPm) }
    var selectedHour by remember { mutableIntStateOf(initialHour) }
    var selectedMinute by remember { mutableIntStateOf(initialMinute) }

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

                Spacer(modifier = Modifier.height(32.dp))

                // 시간 선택기
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 오전/오후
                    TimePickerColumn(
                        value = selectedAmPm,
                        onIncrease = {
                        selectedAmPm = if (selectedAmPm == "오전") "오후" else "오전"
                        },
                        onDecrease = {
                        selectedAmPm = if (selectedAmPm == "오전") "오후" else "오전"
                        }
                    )

                    // 시간
                    TimePickerColumn(
                        value = "${selectedHour}시",
                        onIncrease = {
                        selectedHour = if (selectedHour == 12) 1 else selectedHour + 1
                        },
                        onDecrease = {
                        selectedHour = if (selectedHour == 1) 12 else selectedHour - 1
                        }
                    )

                    // 분
                    TimePickerColumn(
                        value = "${String.format("%02d", selectedMinute)}분",
                        onIncrease = {
                        selectedMinute = if (selectedMinute == 30) 0 else 30
                        },
                        onDecrease = {
                        selectedMinute = if (selectedMinute == 0) 30 else 0
                        }
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // 확인 버튼
                LightonButton(
                    text = "확인",
                    onClick = {
                        val timeString = formatTime(selectedAmPm, selectedHour, selectedMinute)
                        onTimeSelected(timeString)
                        onConfirm()
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun TimePickerColumn(
    value: String,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    // 사진과 동일한 레이아웃: 위화살표 + 텍스트 + 아래화살표 (가로 배치)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // 위 화살표 (텍스트 왼쪽)
        Icon(
            painter = painterResource(id = com.luckydut97.lighton.feature_stage_register.R.drawable.ic_small_above_arrow),
            contentDescription = "증가",
            tint = ClickableColor,
            modifier = Modifier
                .size(16.dp)
                .clickable { onIncrease() }
        )

        // 텍스트
        Text(
            text = value,
            fontFamily = PretendardFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            color = Color.Black
        )

        // 아래 화살표 (텍스트 오른쪽)
        Icon(
            painter = painterResource(id = com.luckydut97.lighton.feature_stage_register.R.drawable.ic_small_below_arrow),
            contentDescription = "감소",
            tint = ClickableColor,
            modifier = Modifier
                .size(16.dp)
                .clickable { onDecrease() }
        )
    }
}

private fun parseTime(timeString: String): Triple<String, Int, Int> {
    return try {
        // "16:30" 형식 파싱
        val parts = timeString.split(":")
        val hour24 = parts[0].toInt()
        val minute = parts[1].toInt()

        val amPm = if (hour24 < 12) "오전" else "오후"
        val hour12 = when (hour24) {
            0 -> 12
            in 1..12 -> hour24
            else -> hour24 - 12
        }

        Triple(amPm, hour12, minute)
    } catch (e: Exception) {
        Triple("오후", 6, 0)
    }
}

private fun formatTime(amPm: String, hour: Int, minute: Int): String {
    val hour24 = when {
        amPm == "오전" && hour == 12 -> 0
        amPm == "오후" && hour != 12 -> hour + 12
        else -> hour
    }
    return String.format("%02d:%02d", hour24, minute)
}