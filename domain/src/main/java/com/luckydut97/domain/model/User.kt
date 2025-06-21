package com.luckydut97.domain.model

/**
 * 사용자 도메인 모델
 * 비즈니스 로직의 핵심 사용자 엔티티
 */
data class User(
    val id: String,
    val email: String,
    val name: String,
    val profileImageUrl: String? = null,
    val phoneNumber: String? = null,
    val preferredGenres: List<String> = emptyList(),
    val preferredRegion: String? = null
)