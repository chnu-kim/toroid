package me.chnu.toroid.domain.user

import java.util.UUID

interface TokenGenerator {
    fun generateAccessToken(id: UUID): AccessTokenResponse
    fun generateRefreshToken(): RefreshTokenResponse
}

