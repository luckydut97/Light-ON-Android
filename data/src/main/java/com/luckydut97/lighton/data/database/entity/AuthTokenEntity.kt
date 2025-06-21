package com.luckydut97.lighton.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 인증 토큰을 저장하는 Room Entity
 */
@Entity(tableName = "auth_tokens")
data class AuthTokenEntity(
    @PrimaryKey
    val id: Int = 1, // 단일 사용자이므로 고정값
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
    val expiresAt: Long, // 토큰 만료 시간
    val createdAt: Long = System.currentTimeMillis()
)