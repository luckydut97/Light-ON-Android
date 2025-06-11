package com.luckydut97.lighton.feature_home.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RecommendedPerformance(
    val id: String,
    val imageResId: Int, // 테스트용, 실제로는 imageUrl: String
    val title: String
)

data class ArtistPerformance(
    val id: String,
    val artistName: String,
    val performanceTitle: String,
    val genre: String,
    val imageResId: Int,
    val date: String,   // yyyy.MM.dd
    val time: String,   // HH:mm
    val place: String
)

class HomeViewModel : ViewModel() {

    private val _recommendedPerformances =
        MutableStateFlow<List<RecommendedPerformance>>(emptyList())
    val recommendedPerformances: StateFlow<List<RecommendedPerformance>> = _recommendedPerformances

    private val _featuredArtistPerformances = MutableStateFlow<List<ArtistPerformance>>(emptyList())
    val featuredArtistPerformances: StateFlow<List<ArtistPerformance>> = _featuredArtistPerformances

    init {
        loadRecommendedPerformances()
        loadFeaturedArtistPerformances() // 주목 아티스트 데이터 불러오기
    }

    private fun loadRecommendedPerformances() {
        viewModelScope.launch {
            // 테스트 데이터 - 실제로는 서버에서 받아올 예정
            val testData = listOf(
                RecommendedPerformance(
                    id = "1",
                    imageResId = com.luckydut97.lighton.feature.home.R.drawable.reco_test_img1,
                    title = "[홍대] Light ON 홀리데이 버스킹"
                ),
                RecommendedPerformance(
                    id = "2",
                    imageResId = com.luckydut97.lighton.feature.home.R.drawable.reco_test_img2,
                    title = "[잠실] Light ON 홀리데이 버스킹"
                ),
                RecommendedPerformance(
                    id = "3",
                    imageResId = com.luckydut97.lighton.feature.home.R.drawable.reco_test_img3,
                    title = "[여의도] Light ON 홀리데이 버스킹"
                ),
                RecommendedPerformance(
                    id = "4",
                    imageResId = com.luckydut97.lighton.feature.home.R.drawable.reco_test_img4,
                    title = "[일산] Light ON 홀리데이 버스킹"
                )
            )
            _recommendedPerformances.value = testData
        }
    }

    private fun loadFeaturedArtistPerformances() {
        _featuredArtistPerformances.value = listOf(
            ArtistPerformance(
                id = "1",
                artistName = "라이트온",
                performanceTitle = "[여의도] Light ON 홀리데이 버스킹",
                genre = "어쿠스틱",
                imageResId = com.luckydut97.lighton.feature.home.R.drawable.reco_test_img1,
                date = "2025.05.01",
                time = "17:00",
                place = "서울 영등포구 여의도동 81-8"
            ),
            ArtistPerformance(
                id = "2",
                artistName = "라이트온",
                performanceTitle = "[홍대] 버스킹 나이트",
                genre = "재즈",
                imageResId = com.luckydut97.lighton.feature.home.R.drawable.reco_test_img2,
                date = "2025.06.12",
                time = "19:00",
                place = "서울 마포구 홍익로 20"
            ),
            ArtistPerformance(
                id = "3",
                artistName = "라이트온",
                performanceTitle = "[잠실] 청춘 콘서트",
                genre = "발라드",
                imageResId = com.luckydut97.lighton.feature.home.R.drawable.reco_test_img3,
                date = "2025.07.02",
                time = "18:00",
                place = "서울 송파구 올림픽로 25"
            ),
            ArtistPerformance(
                id = "4",
                artistName = "라이트온",
                performanceTitle = "[일산] 어쿠스틱 night",
                genre = "어쿠스틱",
                imageResId = com.luckydut97.lighton.feature.home.R.drawable.reco_test_img4,
                date = "2025.08.14",
                time = "20:00",
                place = "경기 고양시 일산동구 정발산로 35"
            )
        )
    }

    // 향후 다른 섹션들을 위한 확장 공간
    // private val _otherSection1 = MutableStateFlow<List<...>>(emptyList())
    // private val _otherSection2 = MutableStateFlow<List<...>>(emptyList())
}
