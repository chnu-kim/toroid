package me.chnu.toroid.presentation

import me.chnu.toroid.domain.user.AccessToken
import me.chnu.toroid.domain.user.RefreshToken
import kotlin.time.Duration

data class AuthResponse(
    val accessToken: AccessToken,
    val refreshToken: RefreshToken,
    val accessTokenExpiresIn: Duration,
    val refreshTokenExpiresIn: Duration,
)