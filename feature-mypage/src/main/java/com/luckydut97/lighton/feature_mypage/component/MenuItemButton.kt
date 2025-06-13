package com.luckydut97.lighton.feature_mypage.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.luckydut97.lighton.core.ui.theme.PretendardFamily
import com.luckydut97.lighton.feature.mypage.R

@Composable
fun MenuItemButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val clickableColor = Color(0xFFABABAB)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .height(45.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = PretendardFamily,
            color = Color.Black
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_right_arrow),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = clickableColor
        )
    }
}
