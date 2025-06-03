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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
 * 체크박스 컴포넌트
 */
@Composable
fun LightonCheckbox(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Black,
    actionText: String = "",
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = BrandColor,
                uncheckedColor = Color.Gray
            )
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = text,
            color = textColor,
            fontSize = 14.sp,
            fontFamily = PretendardFamily,
            modifier = Modifier.weight(1f)
        )
        
        if (actionText.isNotEmpty() && onActionClick != null) {
            Text(
                text = actionText,
                color = Color.Gray,
                fontSize = 13.sp,
                fontFamily = PretendardFamily,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clickable(onClick = onActionClick)
                    .padding(vertical = 4.dp, horizontal = 8.dp)
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
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = BrandColor,
                    uncheckedColor = Color.Gray
                )
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
