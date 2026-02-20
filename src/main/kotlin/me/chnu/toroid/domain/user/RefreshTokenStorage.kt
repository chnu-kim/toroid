package me.chnu.toroid.domain.user


interface RefreshTokenStorage {
    fun save(refreshToken: RefreshToken, id: UserPublicId)

    fun findByToken(refreshToken: RefreshToken): UserPublicId?

    fun revoke(refreshToken: RefreshToken)
}
