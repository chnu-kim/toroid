package me.chnu.toroid.domain.user


interface RefreshTokenStorage {
    fun save(refreshToken: RefreshToken, id: UserPublicId)

    fun findAndRevoke(refreshToken: RefreshToken): UserPublicId?
}
