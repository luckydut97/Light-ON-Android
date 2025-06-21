package com.luckydut97.lighton.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.luckydut97.lighton.data.database.dao.AuthTokenDao
import com.luckydut97.lighton.data.database.dao.UserDao
import com.luckydut97.lighton.data.database.entity.AuthTokenEntity
import com.luckydut97.lighton.data.database.entity.UserEntity

/**
 * Lighton 앱의 메인 Room Database
 */
@Database(
    entities = [
        UserEntity::class,
        AuthTokenEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun authTokenDao(): AuthTokenDao

    companion object {
        private const val DATABASE_NAME = "lighton_database"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration() // 개발 단계에서는 간단한 마이그레이션
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
