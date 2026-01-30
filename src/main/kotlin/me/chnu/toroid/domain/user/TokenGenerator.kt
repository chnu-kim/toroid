package me.chnu.toroid.domain.user

interface TokenGenerator {
    fun generateAccessToken(id: PublicId): AccessTokenResponse
    fun generateRefreshToken(): RefreshTokenResponse
}

