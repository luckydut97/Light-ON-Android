package com.luckydut97.lighton.data.storage

interface TokenSaver {
    suspend fun saveTokens(accessToken: String, refreshToken: String)
}