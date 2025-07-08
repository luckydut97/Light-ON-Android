package com.luckydut97.lighton.feature_stage_register.component.time

fun parseTime(timeString: String): Triple<String, Int, Int> {
    return try {
        when {
            // "18:30" 형식 파싱
            timeString.contains(":") -> {
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

fun formatTime(amPm: String, hour: Int, minute: Int): String {
    val hour24 = when {
        amPm == "오전" && hour == 12 -> 0
        amPm == "오후" && hour != 12 -> hour + 12
        else -> hour
    }
    return String.format("%02d:%02d", hour24, minute)
}

fun formatTo24Hour(displayTime: String): String {
    val (amPm, hour, minute) = parseTime(displayTime)
    return formatTime(amPm, hour, minute)
}

fun formatDisplayTime(amPm: String, hour: Int, minute: Int): String {
    return "${amPm} ${hour}시 ${minute}분"
}

fun handleHourChange(
    currentAmPm: String,
    currentHour: Int,
    newHour: Int
): Pair<String, Int> {
    // 시간 변경 시 오전/오후 자동 전환 로직
    val newAmPm = when {
        currentHour == 12 && newHour == 1 -> {
            // 12시 -> 1시로 변경 시 오전/오후 전환
            if (currentAmPm == "오전") "오후" else "오전"
        }

        currentHour == 1 && newHour == 12 -> {
            // 1시 -> 12시로 변경 시 오전/오후 전환
            if (currentAmPm == "오전") "오후" else "오전"
        }

        else -> currentAmPm
    }

    return Pair(newAmPm, newHour)
}