package com.luckydut97.feature_stage.main.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.luckydut97.lighton.core.ui.components.CommonTopBar
import com.luckydut97.feature_stage.component.StageTabBar
import com.luckydut97.feature_stage.component.StageTab

@Composable
fun StageScreen(
    onBackClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(StageTab.POPULAR) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        CommonTopBar(title = "공연 목록", onBackClick = onBackClick)
        StageTabBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )
        // FilterChips, PerformanceList 자리
    }
}
