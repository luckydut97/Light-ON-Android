package com.luckydut97.feature_home.main.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.luckydut97.feature_home.component.TopBar

@Composable
fun HomeScreen(
    onSearchClick: () -> Unit = {},
    onAlarmClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // TopBar placed at the very top of the screen
            TopBar(
                onSearchClick = onSearchClick,
                onAlarmClick = onAlarmClick
            )

            // Main Hero Section
            com.luckydut97.feature_home.component.HeroImageSection()
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
