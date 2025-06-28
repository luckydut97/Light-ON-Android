package com.luckydut97.lighton.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.lighton.core.ui.theme.BrandColor
import com.luckydut97.lighton.core.ui.theme.PretendardFamily

/**
 * 체크박스 컴포넌트 (새로운 스타일)
 */
@Composable
fun LightonCheckbox(
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    fontSize: Int = 14,
    fontWeight: FontWeight = FontWeight.SemiBold,
    letterSpacing: Int = -1
) {
    Row(
        modifier = modifier.clickable { onCheckedChange(!isChecked) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(
                id = if (isChecked) com.luckydut97.lighton.core.R.drawable.ic_checkbox_ok
                else com.luckydut97.lighton.core.R.drawable.ic_checkbox_no
            ),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color.Unspecified
        )

        if (text.isNotEmpty()) {
            Spacer(modifier = Modifier.width(2.dp))

            Text(
                text = text,
                fontSize = fontSize.sp,
                fontWeight = if (isChecked) FontWeight.Bold else fontWeight,
                color = if (isChecked) BrandColor else Color.Black,
                fontFamily = PretendardFamily,
                letterSpacing = letterSpacing.sp
            )
        }
    }
}

/**
 * 약관 동의에 사용되는 체크박스 컴포넌트
 */
@Composable
fun LightonAgreementCheckbox(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    showDetailButton: Boolean = true,
    onDetailClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(
                    id = if (checked) com.luckydut97.lighton.core.R.drawable.ic_checkbox_ok
                    else com.luckydut97.lighton.core.R.drawable.ic_checkbox_no
                ),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.Unspecified
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = title,
                color = Color.Black,
                fontSize = 14.sp,
                fontFamily = PretendardFamily
            )
        }
        
        if (showDetailButton) {
            Text(
                text = "내용보기",
                color = Color.Gray,
                fontSize = 13.sp,
                fontFamily = PretendardFamily,
                modifier = Modifier
                    .clickable(onClick = onDetailClick)
                    .padding(horizontal = 12.dp)
            )
        }
    }
}

/**
 * Figma-styled 커스텀 체크박스(서버 연동, 의도적으로 OK/NO 아이콘으로 분리)
 */
@Composable
fun LightonIconCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Int = 16, // 16~18 = Figma, 16기본(Figma row padding 모두 적용)
    enabled: Boolean = true
) {
    val iconRes =
        if (checked) com.luckydut97.lighton.core.R.drawable.ic_checkbox_ok else com.luckydut97.lighton.core.R.drawable.ic_checkbox_no
    Image(
        painter = painterResource(iconRes),
        contentDescription = null,
        modifier = modifier
            .size(iconSize.dp)
            .clickable(enabled) { onCheckedChange(!checked) }
    )
}
