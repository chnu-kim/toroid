package me.chnu.toroid.domain.user

interface TokenGenerator {
    fun generateAccessToken(id: UserPublicId): AccessTokenResponse
    fun generateRefreshToken(): RefreshTokenResponse
}

