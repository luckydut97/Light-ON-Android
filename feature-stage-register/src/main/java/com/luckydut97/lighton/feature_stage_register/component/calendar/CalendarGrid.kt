package com.luckydut97.lighton.feature_stage_register.component.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.lighton.core.ui.theme.AssistiveColor
import com.luckydut97.lighton.core.ui.theme.PretendardFamily
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarGrid(
    currentMonth: YearMonth,
    selectedStartDate: LocalDate?,
    selectedEndDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    val today = LocalDate.now()

    // 달력 데이터 생성
    val calendarData = remember(currentMonth) {
        generateCalendarData(currentMonth)
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // 요일 헤더
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(25.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf("일", "월", "화", "수", "목", "금", "토").forEach { dayOfWeek ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dayOfWeek,
                        fontFamily = PretendardFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                        color = AssistiveColor
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 날짜 그리드
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(calendarData) { dateInfo ->
                DateCell(
                    date = dateInfo.date,
                    isCurrentMonth = dateInfo.isCurrentMonth,
                    isToday = dateInfo.date == today,
                    isSelectable = !dateInfo.date.isBefore(today),
                    isStartDate = selectedStartDate == dateInfo.date,
                    isEndDate = selectedEndDate == dateInfo.date,
                    isInRange = isDateInRange(dateInfo.date, selectedStartDate, selectedEndDate),
                    onClick = {
                        if (!dateInfo.date.isBefore(today)) {
                            onDateSelected(dateInfo.date)
                        }
                    }
                )
            }
        }
    }
}

private fun generateCalendarData(yearMonth: YearMonth): List<CalendarDateInfo> {
    val firstDayOfMonth = yearMonth.atDay(1)
    val lastDayOfMonth = yearMonth.atEndOfMonth()
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // 일요일을 0으로 맞춤

    val calendarData = mutableListOf<CalendarDateInfo>()

    // 이전 달 날짜들 (빈 셀)
    val prevMonth = yearMonth.minusMonths(1)
    val prevMonthLastDay = prevMonth.atEndOfMonth()
    for (i in firstDayOfWeek - 1 downTo 0) {
        calendarData.add(
            CalendarDateInfo(
                date = prevMonthLastDay.minusDays(i.toLong()),
                isCurrentMonth = false
            )
        )
    }

    // 현재 달 날짜들
    var currentDate = firstDayOfMonth
    while (currentDate <= lastDayOfMonth) {
        calendarData.add(
            CalendarDateInfo(
                date = currentDate,
                isCurrentMonth = true
            )
        )
        currentDate = currentDate.plusDays(1)
    }

    // 다음 달 날짜들 (6주 완성을 위해)
    val nextMonth = yearMonth.plusMonths(1)
    var nextMonthDate = nextMonth.atDay(1)
    while (calendarData.size < 42) { // 6주 * 7일 = 42
        calendarData.add(
            CalendarDateInfo(
                date = nextMonthDate,
                isCurrentMonth = false
            )
        )
        nextMonthDate = nextMonthDate.plusDays(1)
    }

    return calendarData
}

private fun isDateInRange(date: LocalDate, startDate: LocalDate?, endDate: LocalDate?): Boolean {
    return if (startDate != null && endDate != null) {
        !date.isBefore(startDate) && !date.isAfter(endDate)
    } else {
        false
    }
}

data class CalendarDateInfo(
    val date: LocalDate,
    val isCurrentMonth: Boolean
)