package com.luckydut97.lighton.feature_stage_register.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.lighton.core.ui.theme.*


@Composable
fun CountingInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    maxLength: Int,
    modifier: Modifier = Modifier,
    isRequired: Boolean = false,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true
) {
    var isFocused by remember { mutableStateOf(false) }

    // focus 상태에 따른 색상 결정
    val labelColor = if (isFocused) BrandColor else CaptionColor

    Column(modifier = modifier.fillMaxWidth()) {
        // 라벨 + 필수 표시(*) - 입력 필드 좌측 상단에서 16dp 떨어진 위치
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(18.dp)
                .padding(start = 16.dp)
        ) {
            Text(
                text = label,
                color = labelColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = PretendardFamily
            )

            if (isRequired) {
                Text(
                    text = " *",
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = PretendardFamily
                )
            }
        }

        // 6dp 간격
        Spacer(modifier = Modifier.height(6.dp))

        // 입력 필드 - 47dp 높이
        val textStyle = TextStyle(
            fontFamily = PretendardFamily,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 15.sp
        )

        BasicTextField(
            value = value,
            onValueChange = { newValue ->
                if (newValue.length <= maxLength) {
                    onValueChange(newValue)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(47.dp)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            textStyle = textStyle.copy(color = Color.Black),
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            keyboardActions = KeyboardActions.Default,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.White)
                        .border(
                            width = 1.dp,
                            color = if (isFocused) BrandColor else ThumbLineColor,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = AssistiveColor,
                            fontFamily = PretendardFamily,
                            fontSize = 15.sp,
                            lineHeight = 15.sp,
                            maxLines = 1
                        )
                    }
                    innerTextField()
                }
            }
        )

        // 6dp 간격
        Spacer(modifier = Modifier.height(6.dp))

        // 글자수 카운터 - 우하단 정렬
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(18.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = "${value.length}/$maxLength",
                color = CaptionColor,
                fontSize = 12.sp,
                fontFamily = PretendardFamily,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(end = 4.dp)
            )
        }
    }
}