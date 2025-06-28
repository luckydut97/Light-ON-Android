package com.luckydut97.lighton.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.lighton.core.R
import com.luckydut97.lighton.core.ui.theme.*

@Composable
fun PhoneVerificationField(
    phone: String,
    onPhoneChange: (String) -> Unit,
    verificationCode: String,
    onVerificationCodeChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onVerifyClick: () -> Unit,
    onResendClick: () -> Unit,
    codeTimerSec: Int,
    resendEnabled: Boolean,
    resendCooldownSec: Int = 0,
    phoneFieldEnabled: Boolean = true,
    showCodeField: Boolean = false,
    isVerified: Boolean = false,
    errorMsg: String? = null,
) {
    // 1. 휴대폰번호+인증받기
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom
    ) {
        LightonInputField(
            label = "휴대폰 번호",
            value = phone,
            onValueChange = onPhoneChange,
            isRequired = true,
            placeholder = "휴대폰 번호를 입력해주세요.",
            keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone,
            enableVerifyButton = true, // 항상 활성화
            onVerifyClick = onSendClick,
            modifier = Modifier.weight(1f),
            validationResult = if (isVerified) ValidationResult.Valid else ValidationResult.Initial
        )
    }

    // 2. 인증번호 입력 행 - 인증 완료되지 않았을 때만 표시
    if (showCodeField && !isVerified) {
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            VerificationCodeFieldWithTimer(
                value = verificationCode,
                onValueChange = onVerificationCodeChange,
                remainingTime = codeTimerSec,
                isTimerActive = codeTimerSec > 0,
                enabled = !isVerified,
                placeholder = "",
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            SmallActionButton(
                text = if (resendCooldownSec > 0) "${resendCooldownSec}초" else "재전송",
                onClick = onResendClick,
                backgroundColor = Color.White,
                contentColor = Color.Black,
                borderColor = Color.Gray,
                enabled = resendEnabled && !isVerified,
                modifier = Modifier.size(width = 78.dp, height = 47.dp),
                textColor = Color.Black,
                fontSize = 16,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.width(8.dp))
            SmallActionButton(
                text = "확인",
                onClick = onVerifyClick,
                enabled = !isVerified && verificationCode.length == 6 && codeTimerSec > 0,
                backgroundColor = BrandColor,
                contentColor = Color.White,
                modifier = Modifier.size(width = 64.dp, height = 47.dp)
            )
        }
    }

    // 4. 에러 메시지
    if (!isVerified && !errorMsg.isNullOrBlank()) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = errorMsg,
            color = Color.Red,
            fontSize = 12.sp,
            fontFamily = PretendardFamily,
        )
    }
}

@Composable
fun VerificationCodeFieldWithTimer(
    value: String,
    onValueChange: (String) -> Unit,
    remainingTime: Int,
    isTimerActive: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String = "",
    isError: Boolean = false
) {
    var isFocused by remember { mutableStateOf(false) }
    val borderColor = when {
        isError -> Color.Red
        isFocused -> BrandColor
        else -> ThumbLineColor
    }
    BasicTextField(
        value = value,
        onValueChange = { if (it.length <= 6) onValueChange(it) },
        enabled = enabled,
        textStyle = androidx.compose.ui.text.TextStyle(
            fontSize = 16.sp,
            color = Color.Black,
            fontFamily = PretendardFamily,
            fontWeight = FontWeight.Medium
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
            .onFocusChanged { isFocused = it.isFocused },
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(47.dp)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .border(
                        1.dp,
                        borderColor,
                        androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = AssistiveColor,
                            fontFamily = PretendardFamily,
                            fontSize = 16.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1
                        )
                    }
                    Box(Modifier.weight(1f)) {
                        innerTextField()
                    }
                    if (isTimerActive) {
                        Spacer(modifier = Modifier.width(8.dp))
                        val minutes = remainingTime / 60
                        val seconds = remainingTime % 60
                        Text(
                            text = String.format("%02d:%02d", minutes, seconds),
                            color = BrandColor,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = PretendardFamily,
                            textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
                        )
                    }
                }
            }
        }
    )
}
