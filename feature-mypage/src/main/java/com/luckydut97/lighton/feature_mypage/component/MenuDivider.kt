package com.luckydut97.lighton.feature_mypage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MenuDivider(
    modifier: Modifier = Modifier
) {
    val backgroundGray = Color(0xFFF5F5F5)

    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .height(1.dp)
                .background(backgroundGray)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}