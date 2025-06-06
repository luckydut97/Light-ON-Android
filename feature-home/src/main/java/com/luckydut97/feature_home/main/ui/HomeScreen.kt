package com.luckydut97.lighton.feature_home.main.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luckydut97.lighton.feature_home.component.TopBar
import com.luckydut97.lighton.core.ui.components.BottomNavigationBar
import com.luckydut97.lighton.core.ui.components.NavigationItem

@Composable
fun HomeScreen(
    selectedNavItem: NavigationItem = NavigationItem.HOME,
    onSearchClick: () -> Unit = {},
    onAlarmClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Main content area - only respects status bar padding
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(bottom = 108.dp) // 72dp(nav bar) + 36dp(bottom padding)
        ) {
            // TopBar placed below the status bar
            TopBar(
                onSearchClick = onSearchClick,
                onAlarmClick = onAlarmClick
            )

            // Main Hero Section and other content
            com.luckydut97.lighton.feature_home.component.HeroImageSection(
                modifier = Modifier.fillMaxWidth()
            )
            // Content area below the TopBar
            // Add more components here as needed
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
