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

data class PerformanceDetail(
    val id: String,
    val artistName: String,
    val genre: String,
    val title: String,
    val date: String,
    val time: String,
    val location: String,
    val address: String,
    val price: Int?,
    val isPaid: Boolean,
    val description: String,
    val artistInfo: ArtistInfo,
    val seatTypes: List<String>,
    val entryNotes: List<String>,
    val imageUrl: String,
    val isLiked: Boolean = false
)

data class ArtistInfo(
    val name: String,
    val description: String
)
