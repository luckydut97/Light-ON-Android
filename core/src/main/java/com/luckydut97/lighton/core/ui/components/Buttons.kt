package com.luckydut97.lighton.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.lighton.core.R
import com.luckydut97.lighton.core.ui.theme.BrandColor
import com.luckydut97.lighton.core.ui.theme.DisabledColor
import com.luckydut97.lighton.core.ui.theme.PretendardFamily

/**
 * 공통 버튼
 * 높이: 46px, 라운드 모서리, 브랜드 색상(#6137DD)
 */
@Composable
fun LightonButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = BrandColor,
    contentColor: Color = Color.White,
    borderColor: Color = BrandColor,  // 테두리 색상
    borderWidth: Dp = 1.dp  // 테두리 두께
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(46.dp)
            .border(
                width = borderWidth,
                color = if (enabled) borderColor else borderColor.copy(alpha = 0.5f),
                shape = RoundedCornerShape(6.dp)
            ),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContainerColor = backgroundColor.copy(alpha = 0.5f),
            disabledContentColor = contentColor.copy(alpha = 0.5f)
        )
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = PretendardFamily,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * 버튼의 활성화 상태를 관리하는 다음 버튼 컴포넌트
 * 입력 값이 모두 유효한 경우 활성화, 그렇지 않은 경우 비활성화됨
 */
@Composable
fun LightonNextButton(
    text: String = "다음",
    isEnabled: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = isEnabled,
        modifier = modifier
            .fillMaxWidth()
            .height(47.dp),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = BrandColor,
            contentColor = Color.White,
            disabledContainerColor = DisabledColor,
            disabledContentColor = Color.White
        )
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = PretendardFamily,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * 라이트온 앱의 공통 뒤로가기 버튼 컴포넌트
 * core 모듈의 리소스를 사용
 */
@Composable
fun LightonBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Float = 0.8f // 아이콘 크기 비율
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(25.dp) // 버튼 크기 지정
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_back),
            contentDescription = "뒤로가기",
            tint = Color.Black,
            modifier = Modifier.scale(iconSize) // 아이콘 크기 조절
        )
    }
}

/**
 * 외형만 다른 버전의 라이트온 버튼 (아웃라인 스타일)
 * 보라색(BrandColor) 테두리가 추가된 버전
 */
@Composable
fun LightonOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    borderColor: Color = BrandColor,  // 테두리 색상
    borderWidth: Dp = 1.dp  // 테두리 두께
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(46.dp)
            .border(
                width = borderWidth,
                color = if (enabled) borderColor else borderColor.copy(alpha = 0.5f),
                shape = RoundedCornerShape(6.dp)
            ),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Gray
        )
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = BrandColor,
            fontWeight = FontWeight.Bold,
            fontFamily = PretendardFamily,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

/**
 * 전화 인증, 재전송, 확인 등에 사용할 작은 액션 버튼 컴포넌트
 * 높이: 47px, 라운드 모서리, 다양한 스타일 지원
 */
@Composable
fun SmallActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = BrandColor,
    contentColor: Color = Color.White,
    borderColor: Color? = null,
    textColor: Color? = null,
    fontSize: Int = 16,
    fontWeight: FontWeight = FontWeight.SemiBold
) {
    val finalTextColor =
        textColor ?: if (backgroundColor == Color.White) contentColor else Color.White

    if (borderColor != null && backgroundColor == Color.White) {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier.height(47.dp),
            enabled = enabled,
            shape = RoundedCornerShape(6.dp),
            border = BorderStroke(1.dp, borderColor),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = backgroundColor,
                contentColor = contentColor
            ),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Text(
                text = text,
                fontSize = fontSize.sp,
                color = finalTextColor,
                fontWeight = fontWeight,
                fontFamily = PretendardFamily,
                maxLines = 1
            )
        }
    } else {
        Button(
            onClick = onClick,
            modifier = modifier.height(47.dp),
            enabled = enabled,
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColor,
                contentColor = contentColor,
                disabledContainerColor = backgroundColor,
                disabledContentColor = contentColor
            ),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Text(
                text = text,
                fontSize = fontSize.sp,
                color = finalTextColor,
                fontWeight = fontWeight,
                fontFamily = PretendardFamily,
                maxLines = 1
            )
        }
    }
}
