package com.luckydut97.lighton.feature_stage_register.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.lighton.core.ui.theme.*

@Composable
fun SimpleDropdownField(
    value: String,
    onValueChange: (String) -> Unit,
    items: List<String>,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    onClick: () -> Unit = {}
) {
    var isFocused by remember { mutableStateOf(false) }

    // 입력 필드 - 47dp 높이
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(47.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = if (isFocused) BrandColor else ThumbLineColor,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable {
                isFocused = true
                onClick()
            }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 선택된 값 또는 placeholder 텍스트
            Text(
                text = if (value.isEmpty()) placeholder else value,
                color = if (value.isEmpty()) AssistiveColor else Color.Black,
                fontFamily = PretendardFamily,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )

            // 드롭다운 아이콘
            Icon(
                painter = painterResource(id = com.luckydut97.lighton.feature_stage_register.R.drawable.ic_below_arrow),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = AssistiveColor
            )
        }
    }
}