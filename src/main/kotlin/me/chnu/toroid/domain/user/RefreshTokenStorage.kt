package me.chnu.toroid.domain.user

import java.util.*
import kotlin.time.Duration


interface RefreshTokenStorage {
    fun save(refreshToken: RefreshToken, id: UUID)

    fun findByToken(refreshToken: RefreshToken): UUID?

    fun revoke(refreshToken: RefreshToken)
}