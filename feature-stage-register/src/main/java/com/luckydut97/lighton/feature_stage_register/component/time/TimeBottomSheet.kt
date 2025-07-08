package com.luckydut97.lighton.feature_stage_register.component.time

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    onConfirm: () -> Unit,
    startTime: String = "" // 시작시간 정보 추가
) {
    // "오후 6시 30분" 형식의 문자열을 파싱하는 함수
    fun parseTime(timeString: String): Triple<String, Int, Int> {
        return try {
            when {
                // "18:30" 형식 파싱
                timeString.contains(":") -> {
                    val parts = timeString.split(":")
                    val hour24 = parts[0].toInt()
                    val minute = parts[1].toInt()

                    val (amPm, hour12) = when (hour24) {
                        0 -> "오전" to 12        // 00:xx → 오전 12시
                        in 1..11 -> "오전" to hour24   // 01:xx-11:xx → 오전 1시-11시
                        12 -> "오후" to 12       // 12:xx → 오후 12시
                        else -> "오후" to (hour24 - 12) // 13:xx-23:xx → 오후 1시-11시
                    }

                    Triple(amPm, hour12, minute)
                }
                // "오후 6시 30분" 형식 파싱
                timeString.contains("시") -> {
                    val parts = timeString.split(" ")
                    val amPm = parts[0]
                    val timePart = parts[1]

                    val hourStr = timePart.substringBefore("시")
                    val minuteStr = timePart.substringAfter("시").trim().substringBefore("분").trim()

                    val hour = hourStr.toInt()
                    val minute = if (minuteStr.isEmpty()) 0 else minuteStr.toInt()

                    Triple(amPm, hour, minute)
                }

                else -> Triple("오후", 6, 0)
            }
        } catch (e: Exception) {
            Triple("오후", 6, 0)
        }
    }

    // 24시간 형식으로 변환하는 함수
    fun formatTime(amPm: String, hour: Int, minute: Int): String {
        val hour24 = when {
            amPm == "오전" && hour == 12 -> 0  // 오전 12시는 00시
            amPm == "오전" -> hour              // 오전 1시-11시는 그대로
            amPm == "오후" && hour == 12 -> 12 // 오후 12시는 12시
            amPm == "오후" -> hour + 12         // 오후 1시-11시는 +12
            else -> hour
        }
        return String.format("%02d:%02d", hour24, minute)
    }

    // 초기값 계산 (BottomSheet가 열릴 때마다 새로 계산)
    val initialValues = remember(isVisible, selectedTime, startTime, title) {
        if (selectedTime.isEmpty()) {
            // 시작시간이 빈 값이면 "오후 06시 00분"을 디폴트로 설정
            if (title.contains("시작")) {
                Triple("오후", 6, 0)
            } else {
                // 종료시간이 빈 값이고 시작시간이 설정되어 있으면 시작시간 + 1시간
                if (startTime.isNotEmpty()) {
                    val (startAmPm, startHour, startMinute) = parseTime(startTime)
                    // 1시간 더한 시간 계산
                    var endHour = startHour + 1
                    var endAmPm = startAmPm

                    if (endHour > 12) {
                        endHour = 1
                        endAmPm = if (startAmPm == "오전") "오후" else "오전"
                    }

                    Triple(endAmPm, endHour, startMinute)
                } else {
                    // 시작시간이 없으면 "오후 07시 00분"을 디폴트로 설정
                    Triple("오후", 7, 0)
                }
            }
        } else {
            parseTime(selectedTime)
        }
    }

    // 상태 변수들 - BottomSheet가 열릴 때마다 초기화
    var selectedAmPm by remember(isVisible) { mutableStateOf(initialValues.first) }
    var selectedHour by remember(isVisible) { mutableIntStateOf(initialValues.second) }
    var selectedMinute by remember(isVisible) { mutableIntStateOf(initialValues.third) }

    // BottomSheet가 열릴 때 초기값 설정
    LaunchedEffect(isVisible, initialValues) {
        if (isVisible) {
            selectedAmPm = initialValues.first
            selectedHour = initialValues.second
            selectedMinute = initialValues.third
        }
    }

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
                // 드래그 핸들만 드래그 가능하도록 수정
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(24.dp)
                        .background(Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(4.dp)
                            .background(
                                color = Color.Gray.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(2.dp)
                            )
                            .align(Alignment.Center)
                    )
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = title,
                    fontFamily = PretendardFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = BrandColor,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // iOS 스타일 휠 시간 선택기
                WheelTimePicker(
                    amPm = selectedAmPm,
                    hour = selectedHour,
                    minute = selectedMinute,
                    onAmPmChanged = { newAmPm ->
                        selectedAmPm = newAmPm
                    },
                    onHourChanged = { newHour ->
                        selectedHour = newHour
                    },
                    onMinuteChanged = { newMinute ->
                        selectedMinute = newMinute
                    }
                )

                Spacer(modifier = Modifier.height(40.dp))

                // 확인 버튼
                LightonButton(
                    text = "확인",
                    onClick = {
                        // formatTime 함수를 사용하여 24시간 형식으로 변환
                        val timeString = formatTime(selectedAmPm, selectedHour, selectedMinute)

                        // 디버그 로그 - 최종 선택값 확인
                        println("TimeBottomSheet - 최종 선택값: $selectedAmPm ${selectedHour}시 ${selectedMinute}분 -> $timeString")

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