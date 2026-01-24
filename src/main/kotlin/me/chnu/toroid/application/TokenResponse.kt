package me.chnu.toroid.application

import me.chnu.toroid.domain.user.AccessToken
import me.chnu.toroid.domain.user.RefreshToken
import kotlin.time.Duration

data class TokenResponse(
    val accessToken: AccessToken,
    val refreshToken: RefreshToken,
    val accessTokenExpiresIn: Duration,
    val refreshTokenExpiresIn: Duration,
)