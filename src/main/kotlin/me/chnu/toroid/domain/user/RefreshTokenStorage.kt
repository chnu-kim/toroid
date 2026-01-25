package me.chnu.toroid.domain.user

import java.util.*
import kotlin.time.Duration


interface RefreshTokenStorage {
    fun save(refreshToken: RefreshToken, id: PublicId)

    fun findByToken(refreshToken: RefreshToken): PublicId?

    fun revoke(refreshToken: RefreshToken)
}