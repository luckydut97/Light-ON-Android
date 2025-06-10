package com.luckydut97.lighton.feature_home.main.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luckydut97.feature_home.component.TopBar
import com.luckydut97.feature_home.component.RecommendationSection
import com.luckydut97.feature_home.component.HeroImageSection
import com.luckydut97.lighton.feature_home.main.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    onSearchClick: () -> Unit = {},
    onAlarmClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel()
) {
    val recommendedPerformances by viewModel.recommendedPerformances.collectAsState()
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Main content area - only respects status bar padding
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 108.dp) // 72dp(nav bar) + 36dp(bottom padding)
                .verticalScroll(scrollState)
        ) {
            // TopBar placed below the status bar
            TopBar(
                onSearchClick = onSearchClick,
                onAlarmClick = onAlarmClick
            )

            // Main Hero Section
            HeroImageSection(
                modifier = Modifier.fillMaxWidth()
            )

            // Recommendation Section
            RecommendationSection(
                performances = recommendedPerformances,
                onMoreClick = {
                    // TODO: 추천 공연 더보기 페이지로 이동
                },
                onPerformanceClick = { performance ->
                    // TODO: 개별 공연 상세 페이지로 이동
                },
                modifier = Modifier.fillMaxWidth()
            )

            // 향후 추가될 섹션들을 위한 공간
            // 예: FeaturedEventSection(), PopularArtistSection() 등
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
