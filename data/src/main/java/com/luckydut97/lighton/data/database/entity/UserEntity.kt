package com.luckydut97.lighton.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.luckydut97.domain.model.User

/**
 * 사용자 정보를 저장하는 Room Entity
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val email: String,
    val name: String,
    val profileImageUrl: String? = null,
    val phoneNumber: String? = null,
    val preferredRegion: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * UserEntity를 Domain User로 변환
 */
fun UserEntity.toDomain(): User {
    return User(
        id = id,
        email = email,
        name = name,
        profileImageUrl = profileImageUrl,
        phoneNumber = phoneNumber,
        preferredGenres = emptyList(), // 별도 테이블에서 관리
        preferredRegion = preferredRegion
    )
}

/**
 * Domain User를 UserEntity로 변환
 */
fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        email = email,
        name = name,
        profileImageUrl = profileImageUrl,
        phoneNumber = phoneNumber,
        preferredRegion = preferredRegion,
        updatedAt = System.currentTimeMillis()
    )
}