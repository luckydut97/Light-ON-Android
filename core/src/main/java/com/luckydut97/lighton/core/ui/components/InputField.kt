package com.luckydut97.lighton.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.lighton.core.ui.theme.BrandColor
import com.luckydut97.lighton.core.ui.theme.PlaceholderTextColor
import com.luckydut97.lighton.core.ui.theme.TextFieldBorderColor
import com.luckydut97.lighton.core.ui.theme.AssistiveColor
import com.luckydut97.lighton.core.ui.theme.InfoTextColor
import com.luckydut97.lighton.core.ui.theme.ThumbLineColor
import com.luckydut97.lighton.core.ui.theme.DistructiveColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.SpanStyle
import com.luckydut97.lighton.core.ui.theme.CaptionColor
import com.luckydut97.lighton.core.ui.theme.PretendardFamily

/**
 * 사용자 정의 비밀번호 변환 - 14x14 검정 원점 사용
 */
class CustomPasswordVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // 14dp 크기의 검정 원점을 위한 스타일링된 문자열 생성
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
 * 입력 필드 유효성 검사 결과 상태
 */
sealed class ValidationResult {
    object Valid : ValidationResult()
    object Initial : ValidationResult()
    data class Invalid(val message: String) : ValidationResult()
}

/**
 * 공통 입력 필드 컴포넌트
 * 라벨, 필수 여부(*), 입력 필드, 유효성 검사 결과 메시지를 표시
 * 전체 높이: 94dp (17dp + 6dp + 47dp + 6dp + 18dp)
 */
@Composable
fun LightonInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isRequired: Boolean = false,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    validationResult: ValidationResult = ValidationResult.Initial,
    enableVerifyButton: Boolean = false,
    onVerifyClick: (() -> Unit)? = null
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
                color = labelColor, // focus 상태에 따른 색상 적용
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = PretendardFamily
            )

            if (isRequired) {
                Text(
                    text = " *",
                    color = Color.Red, // 항상 빨간색 유지
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = PretendardFamily
                )
            }
        }

        // 6dp 간격
        Spacer(modifier = Modifier.height(6.dp))

        // 입력 필드와 중복확인 버튼 (있는 경우) - 47dp 높이
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            val textStyle = TextStyle(
                fontFamily = PretendardFamily,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 15.sp
                // color 제거 - OutlinedTextField의 colors에서 관리
            )

            val interactionSource = remember { MutableInteractionSource() }

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .height(47.dp)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                textStyle = textStyle.copy(color = if (isPassword) DistructiveColor else Color.Black),
                singleLine = true,
                visualTransformation = if (isPassword) CustomPasswordVisualTransformation() else VisualTransformation.None,
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

            // 휴대폰 인증 버튼은 항상 표시, 다른 버튼은 enableVerifyButton이 true일 때만 표시
            if ((label.contains("휴대폰") || enableVerifyButton) && onVerifyClick != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onVerifyClick,
                    enabled = enableVerifyButton, // 활성/비활성 제어
                    modifier = Modifier
                        .height(47.dp) // 텍스트필드와 동일한 높이
                        .width(91.dp), // width 91dp로 고정
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandColor,
                        contentColor = Color.White,
                        disabledContainerColor = BrandColor,
                        disabledContentColor = Color.White
                    )
                ) {
                    Text(
                        text = if (label.contains("휴대폰")) "인증 받기" else "중복확인",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        fontFamily = PretendardFamily
                    )
                }
            }
        }

        // 6dp 간격
        Spacer(modifier = Modifier.height(6.dp))

        // 유효성 검사 결과 메시지 - 18dp 높이
        when (validationResult) {
            is ValidationResult.Valid -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .height(18.dp)
                        .fillMaxWidth()
                        .padding(start = 4.dp)
                ) {
                    Box(
                        modifier = Modifier.size(18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = com.luckydut97.lighton.core.R.drawable.ic_check),
                            contentDescription = "Valid",
                            modifier = Modifier
                                .size(18.dp)
                                .offset(y = 3.dp),
                            tint = BrandColor
                        )
                    }
                    Spacer(modifier = Modifier.width(2.dp))
                    Box(
                        modifier = Modifier.height(18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when {
                                label.contains("아이디") -> "사용 가능한 아이디입니다."
                                label.contains("휴대폰") -> "인증이 완료되었습니다."
                                isPassword -> "사용 가능한 비밀번호입니다."
                                else -> "사용 가능합니다."
                            },
                            color = BrandColor,
                            fontSize = 12.sp,
                            fontFamily = PretendardFamily
                        )
                    }
                }
            }

            is ValidationResult.Invalid -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .height(18.dp)
                        .fillMaxWidth()
                        .padding(start = 4.dp)
                ) {
                    Box(
                        modifier = Modifier.size(18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = com.luckydut97.lighton.core.R.drawable.ic_alert),
                            contentDescription = "Error",
                            modifier = Modifier
                                .size(18.dp)
                                .offset(y = 3.dp), // ★ 여기가 핵심입니다 ★
                            tint = Color.Red
                        )
                    }
                    Spacer(modifier = Modifier.width(2.dp))
                    Box(
                        modifier = Modifier.height(18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = validationResult.message,
                            color = Color.Red,
                            fontSize = 12.sp,
                            fontFamily = PretendardFamily
                        )
                    }
                }
            }
            ValidationResult.Initial -> {
                // 초기 상태에서는 메시지 없음 - 18dp 높이 유지를 위해 빈 Spacer
                Spacer(modifier = Modifier.height(18.dp))
            }
        }
    }
}
