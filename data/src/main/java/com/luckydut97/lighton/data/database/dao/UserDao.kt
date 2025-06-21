package com.luckydut97.lighton.data.database.dao

import androidx.room.*
import com.luckydut97.lighton.data.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * 사용자 정보 데이터 접근 객체
 */
@Dao
interface UserDao {

    /**
     * 사용자 정보 조회
     */
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: String): UserEntity?

    /**
     * 현재 사용자 정보 조회 (최신 1개)
     */
    @Query("SELECT * FROM users ORDER BY updatedAt DESC LIMIT 1")
    suspend fun getCurrentUser(): UserEntity?

    /**
     * 현재 사용자 정보 Flow로 관찰
     */
    @Query("SELECT * FROM users ORDER BY updatedAt DESC LIMIT 1")
    fun getCurrentUserFlow(): Flow<UserEntity?>

    /**
     * 이메일로 사용자 조회
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    /**
     * 사용자 정보 저장 (Insert or Update)
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    /**
     * 사용자 정보 업데이트
     */
    @Update
    suspend fun updateUser(user: UserEntity)

    /**
     * 사용자 정보 삭제
     */
    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: String)

    /**
     * 모든 사용자 정보 삭제 (로그아웃 시)
     */
    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
}
