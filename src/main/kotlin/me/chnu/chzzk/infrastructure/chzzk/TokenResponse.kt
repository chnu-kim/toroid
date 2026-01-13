package me.chnu.chzzk.infrastructure.chzzk

data class TokenResponse(
    val refreshToken: String,
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Int,
    val scope: String,
)