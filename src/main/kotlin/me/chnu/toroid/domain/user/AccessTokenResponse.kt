package me.chnu.toroid.domain.user

import kotlin.time.Duration

data class AccessTokenResponse(val accessToken: AccessToken, val expiresIn: Duration)