package com.luckydut97.lighton.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import com.luckydut97.lighton.core.ui.theme.BrandColor
import com.luckydut97.lighton.core.ui.theme.PlaceholderTextColor
import com.luckydut97.lighton.core.ui.theme.PretendardFamily
import com.luckydut97.lighton.core.ui.theme.TextFieldBorderColor

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
    Column(modifier = modifier.fillMaxWidth()) {
        // 라벨 + 필수 표시(*)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = PretendardFamily,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (isRequired) {
                Text(
                    text = " *",
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = PretendardFamily,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }

        // 입력 필드와 중복확인 버튼 (있는 경우)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            val textStyle = TextStyle(
                fontFamily = PretendardFamily,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            val interactionSource = remember { MutableInteractionSource() }

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        text = placeholder,
                        color = PlaceholderTextColor,
                        fontFamily = PretendardFamily,
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White, RoundedCornerShape(8.dp)),
                enabled = true,
                readOnly = false,
                textStyle = textStyle,
                singleLine = true,
                visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                keyboardActions = KeyboardActions.Default,
                interactionSource = interactionSource,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandColor,
                    unfocusedBorderColor = TextFieldBorderColor,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            if (enableVerifyButton && onVerifyClick != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onVerifyClick,
                    modifier = Modifier.height(47.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "중복확인",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontFamily = PretendardFamily
                    )
                }
            }
        }

        // 유효성 검사 결과 메시지
        when (validationResult) {
            is ValidationResult.Valid -> {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                ) {
                    Text(
                        text = "✓",
                        color = BrandColor,
                        modifier = Modifier.padding(end = 4.dp),
                        fontWeight = FontWeight.Bold
                    )

                    // 필드 타입에 따라 다른 메시지 표시
                    val successMessage = when {
                        label.contains("아이디") -> "사용 가능한 아이디입니다."
                        isPassword -> "사용 가능한 비밀번호입니다."
                        else -> "사용 가능합니다."
                    }

                    Text(
                        text = successMessage,
                        color = BrandColor,
                        fontSize = 13.sp,
                        fontFamily = PretendardFamily
                    )
                }
            }
            is ValidationResult.Invalid -> {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                ) {
                    Text(
                        text = "!",
                        color = Color.Red,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = validationResult.message,
                        color = Color.Red,
                        fontSize = 13.sp,
                        fontFamily = PretendardFamily
                    )
                }
            }
            ValidationResult.Initial -> {
                // 초기 상태에서는 메시지 없음
            }
        }
    }
}