package com.luckydut97.domain.model

/**
 * 공연 도메인 모델
 * 비즈니스 로직의 핵심 공연 엔티티
 */
data class Performance(
    val id: String,
    val title: String,
    val artistName: String,
    val genre: String,
    val date: String,
    val time: String,
    val location: String,
    val address: String,
    val price: Int?,
    val isPaid: Boolean,
    val imageUrl: String,
    val description: String = "",
    val isBookmarked: Boolean = false
)

/**
 * 아티스트 도메인 모델
 */
data class Artist(
    val id: String,
    val name: String,
    val description: String,
    val genres: List<String> = emptyList(),
    val imageUrl: String? = null
)