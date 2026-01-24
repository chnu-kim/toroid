package me.chnu.toroid.domain.user

import kotlin.time.Duration

data class RefreshTokenResponse(val refreshToken: RefreshToken, val expiresIn: Duration)