package com.luckydut97.feature_stage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StageTabBar(
    selectedTab: StageTab,
    onTabSelected: (StageTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val brand = Color(0xFF6137DD) // BrandColor
    val assistive = Color(0xFFC4C4C4) // AssistiveColor

    Column(modifier = modifier.background(Color.White)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color.White),
            verticalAlignment = Alignment.Bottom
        ) {
            StageTab.values().forEach { tab ->
                val isSelected = tab == selectedTab
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTabSelected(tab) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = tab.title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) brand else assistive,
                        // fontFamily = Pretendard
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        Modifier
                            .height(2.dp)
                            .fillMaxWidth(1f)
                            .background(if (isSelected) brand else assistive)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(13.dp))
    }
}

enum class StageTab(val title: String) {
    POPULAR("인기 공연"),
    RECOMMENDED("추천 공연")
}
