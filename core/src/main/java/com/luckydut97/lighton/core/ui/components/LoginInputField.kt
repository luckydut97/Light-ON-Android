package com.luckydut97.lighton.core.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.sp
import com.luckydut97.lighton.core.ui.theme.BrandColor
import com.luckydut97.lighton.core.ui.theme.InfoTextColor
import com.luckydut97.lighton.core.ui.theme.ThumbLineColor
import com.luckydut97.lighton.core.ui.theme.DistructiveColor
import com.luckydut97.lighton.core.ui.theme.AssistiveColor
import com.luckydut97.lighton.core.ui.theme.PretendardFamily

/**
 * 사용자 정의 비밀번호 변환 - 14x14 검정 원점 사용 (LoginInputField용)
 */
class LoginCustomPasswordVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // 14dp 크기의 검정 원점과 4dp 간격을 위한 스타일링된 문자열 생성
        val maskedText = AnnotatedString(
            text = "●".repeat(text.text.length), // 더 큰 원점 문자 사용
            spanStyles = listOf(
                AnnotatedString.Range(
                    SpanStyle(
                        fontSize = 14.sp,
                        color = Color.Black,
                        letterSpacing = 4.sp
                    ),
                    0,
                    text.text.length
                )
            )
        )
        return TransformedText(maskedText, OffsetMapping.Identity)
    }
}

/**
 * 로그인 화면 전용 입력 필드 컴포넌트
 * 타이틀 + 텍스트필드 구성
 * 전체 높이: 71dp (18dp 타이틀 + 6dp 간격 + 47dp 텍스트필드)
 */
@Composable
fun LoginInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    isFocused: Boolean = false,
    onFocusChanged: (Boolean) -> Unit = {}
) {
    var isFocusedLocal by remember { mutableStateOf(isFocused) }

    // 외부 isFocused 값과 내부 isFocusedLocal 동기화
    LaunchedEffect(isFocused) {
        isFocusedLocal = isFocused
    }

    // focus 상태에 따른 색상 결정
    val titleColor = if (isFocusedLocal) BrandColor else InfoTextColor

    Column(modifier = modifier.fillMaxWidth()) {
        // 타이틀 텍스트 - 18dp 높이
        Text(
            text = label,
            color = titleColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = PretendardFamily,
            modifier = Modifier
                .height(18.dp)
                .padding(start = 16.dp)
        )

        // 6dp 간격
        Spacer(modifier = Modifier.height(6.dp))

        // 텍스트 필드 - 41dp 높이 (outline 포함)
        val textStyle = TextStyle(
            fontFamily = PretendardFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 16.sp
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(47.dp)
                .onFocusChanged { focusState ->
                    isFocusedLocal = focusState.isFocused
                    onFocusChanged(focusState.isFocused)
                },
            singleLine = true,
            textStyle = textStyle.copy(color = if (isPassword) DistructiveColor else Color.Black),
            visualTransformation = if (isPassword) LoginCustomPasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            keyboardActions = KeyboardActions.Default,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .border(
                            width = 1.dp,
                            color = if (isFocusedLocal) BrandColor else ThumbLineColor,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = AssistiveColor,
                            fontFamily = PretendardFamily,
                            fontSize = 16.sp,
                            lineHeight = 16.sp,
                            maxLines = 1
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}
