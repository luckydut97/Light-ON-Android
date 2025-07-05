package com.luckydut97.feature_stage.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.luckydut97.lighton.core.ui.components.CommonTopBar
import com.luckydut97.feature_stage.component.StageTabBar
import com.luckydut97.feature_stage.component.StageTab
import com.luckydut97.feature_stage.component.FilterChipRow
import com.luckydut97.feature_stage.component.PerformanceItem
import com.luckydut97.feature_stage.model.FilterChip
import com.luckydut97.feature_stage.model.Performance

@Composable
fun StageScreen(
    onBackClick: () -> Unit = {},
    onPerformanceClick: (String) -> Unit = {},
    initialTab: String = "popular"
) {
    val selectedTabValue = when (initialTab) {
        "popular" -> StageTab.POPULAR
        "recommended" -> StageTab.RECOMMENDED
        else -> StageTab.POPULAR
    }

    var selectedTab by remember { mutableStateOf(selectedTabValue) }
    var selectedChipId by remember { mutableStateOf("all") }

    // 더미 필터 칩 데이터
    val filterChips = remember {
        listOf(
            FilterChip("all", "전체", true),
            FilterChip("acoustic", "어쿠스틱", false),
            FilterChip("jazz", "재즈", false),
            FilterChip("hiphop", "힙합", false),
            FilterChip("band", "밴드", false),
            FilterChip("genre1", "장르1", false),
            FilterChip("genre2", "장르2", false),
            FilterChip("genre3", "장르3", false)
        )
    }

    // 선택된 칩 업데이트
    val updatedChips = filterChips.map { chip ->
        chip.copy(isSelected = chip.id == selectedChipId)
    }

    // 더미 공연 데이터
    val popularPerformances = remember {
        listOf(
            Performance(
                id = "1",
                title = "여의도 Light ON 홀리데이 버스킹",
                location = "여의도동",
                date = "2025.07.05",
                time = "17:00",
                imageUrl = "ic_test_img",
                tag = "어쿠스틱"
            ),
            Performance(
                id = "2",
                title = "까페골목 SSUM 타는 콘서트",
                location = "방배동",
                date = "2025.07.05",
                time = "17:00",
                imageUrl = "ic_test_img4",
                tag = "밴드"
            ),
            Performance(
                id = "3",
                title = "광장동 Piano Class Concert",
                location = "광장동",
                date = "2025.07.08",
                time = "17:00",
                imageUrl = "ic_test_img3",
                tag = "클래식"
            ),
            Performance(
                id = "4",
                title = "2025 여의도 불빛무대 눕콘",
                location = "여의도동",
                date = "2025.07.11",
                time = "17:00",
                imageUrl = "ic_test_img1",
                tag = "어쿠스틱",
                overlayTag = "무료공연"
            )/*,
            Performance(
                id = "5",
                title = "싸이키 비주얼 밴드",
                location = "여의도동",
                date = "2025.07.13",
                time = "17:00",
                imageUrl = "ic_test_img2",
                tag = "밴드"
            )*/
        )
    }

    val recommendedPerformances = remember {
        listOf(
            Performance(
                id = "6",
                title = "추천 공연 1",
                location = "강남구",
                date = "2025.05.02",
                time = "19:00",
                imageUrl = "ic_test_img2",
                tag = "재즈"
            ),
            Performance(
                id = "7",
                title = "추천 공연 2",
                location = "서초구",
                date = "2025.05.03",
                time = "20:00",
                imageUrl = "ic_test_img3",
                tag = "어쿠스틱",
                overlayTag = "무료공연"
            ),
            Performance(
                id = "8",
                title = "추천 공연 3",
                location = "마포구",
                date = "2025.05.04",
                time = "18:00",
                imageUrl = "ic_test_img4",
                tag = "힙합"
            )
        )
    }

    // 현재 탭에 따른 공연 목록
    val currentPerformances = if (selectedTab == StageTab.POPULAR) {
        popularPerformances
    } else {
        recommendedPerformances
    }

    // 필터링된 공연 목록
    val filteredPerformances = if (selectedChipId == "all") {
        currentPerformances
    } else {
        currentPerformances.filter { performance ->
            when (selectedChipId) {
                "acoustic" -> performance.tag == "어쿠스틱"
                "jazz" -> performance.tag == "재즈"
                "hiphop" -> performance.tag == "힙합"
                "band" -> performance.tag == "밴드"
                "genre1" -> performance.tag == "장르1"
                "genre2" -> performance.tag == "장르2"
                "genre3" -> performance.tag == "장르3"
                else -> true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        CommonTopBar(
            title = "공연 목록",
            onBackClick = onBackClick
        )
        StageTabBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )
        FilterChipRow(
            chips = updatedChips,
            onChipClick = { chipId -> selectedChipId = chipId }
        )

        // PerformanceList
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredPerformances) { performance ->
                PerformanceItem(
                    performance = performance,
                    onClick = { onPerformanceClick(performance.id) }
                )
            }
        }
    }
}
