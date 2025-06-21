package com.luckydut97.domain.repository

import com.luckydut97.domain.model.Performance
import kotlinx.coroutines.flow.Flow

/**
 * 공연 관련 Domain Repository 인터페이스
 * Data Layer에서 이 인터페이스를 구현해야 함
 */
interface PerformanceRepository {
    /**
     * 인기 공연 목록 가져오기
     */
    suspend fun getPopularPerformances(): Flow<Result<List<Performance>>>

    /**
     * 추천 공연 목록 가져오기
     */
    suspend fun getRecommendedPerformances(): Flow<Result<List<Performance>>>

    /**
     * 공연 상세 정보 가져오기
     */
    suspend fun getPerformanceDetail(performanceId: String): Flow<Result<Performance>>

    /**
     * 장르별 공연 목록 가져오기
     */
    suspend fun getPerformancesByGenre(genre: String): Flow<Result<List<Performance>>>

    /**
     * 공연 북마크 토글
     */
    suspend fun toggleBookmark(performanceId: String): Result<Unit>

    /**
     * 북마크된 공연 목록 가져오기
     */
    suspend fun getBookmarkedPerformances(): Flow<Result<List<Performance>>>
}