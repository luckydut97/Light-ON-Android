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

class HomeViewModel : ViewModel() {

    private val _recommendedPerformances =
        MutableStateFlow<List<RecommendedPerformance>>(emptyList())
    val recommendedPerformances: StateFlow<List<RecommendedPerformance>> = _recommendedPerformances

    init {
        loadRecommendedPerformances()
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

    // 향후 다른 섹션들을 위한 확장 공간
    // private val _otherSection1 = MutableStateFlow<List<...>>(emptyList())
    // private val _otherSection2 = MutableStateFlow<List<...>>(emptyList())
}
