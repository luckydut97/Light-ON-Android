package com.luckydut97.lighton.core.data.storage

interface TokenSaver {
    suspend fun saveTokens(accessToken: String, refreshToken: String)
}