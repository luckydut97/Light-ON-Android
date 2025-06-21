package com.luckydut97.domain.usecase

import com.luckydut97.domain.model.Performance
import com.luckydut97.domain.repository.PerformanceRepository
import kotlinx.coroutines.flow.Flow

/**
 * 인기 공연 목록 가져오기 UseCase
 */
class GetPopularPerformancesUseCase(
    private val performanceRepository: PerformanceRepository
) {
    suspend operator fun invoke(): Flow<Result<List<Performance>>> {
        return performanceRepository.getPopularPerformances()
    }
}

/**
 * 추천 공연 목록 가져오기 UseCase
 */
class GetRecommendedPerformancesUseCase(
    private val performanceRepository: PerformanceRepository
) {
    suspend operator fun invoke(): Flow<Result<List<Performance>>> {
        return performanceRepository.getRecommendedPerformances()
    }
}

/**
 * 공연 상세 정보 가져오기 UseCase
 */
class GetPerformanceDetailUseCase(
    private val performanceRepository: PerformanceRepository
) {
    suspend operator fun invoke(performanceId: String): Flow<Result<Performance>> {
        return performanceRepository.getPerformanceDetail(performanceId)
    }
}

/**
 * 공연 북마크 토글 UseCase
 */
class ToggleBookmarkUseCase(
    private val performanceRepository: PerformanceRepository
) {
    suspend operator fun invoke(performanceId: String): Result<Unit> {
        return performanceRepository.toggleBookmark(performanceId)
    }
}

/**
 * 북마크된 공연 목록 가져오기 UseCase
 */
class GetBookmarkedPerformancesUseCase(
    private val performanceRepository: PerformanceRepository
) {
    suspend operator fun invoke(): Flow<Result<List<Performance>>> {
        return performanceRepository.getBookmarkedPerformances()
    }
}