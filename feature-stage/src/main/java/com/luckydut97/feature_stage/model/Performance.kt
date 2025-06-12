package com.luckydut97.feature_stage.model

data class Performance(
    val id: String,
    val title: String,
    val location: String,
    val date: String,
    val time: String,
    val imageUrl: String,
    val tag: String? = null, // 해시태그 (힙합, 어쿠스틱 등)
    val overlayTag: String? = null // 썸네일 위 태그 (무료공연 등)
)

data class FilterChip(
    val id: String,
    val name: String,
    val isSelected: Boolean = false
)