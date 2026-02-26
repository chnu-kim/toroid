package me.chnu.toroid.application

import me.chnu.toroid.domain.user.RefreshToken
import me.chnu.toroid.domain.user.RefreshTokenStorage
import me.chnu.toroid.domain.user.TokenGenerator
import me.chnu.toroid.domain.user.UserService
import org.springframework.stereotype.Service

@Service
class TokenRefreshUseCase(
    private val userService: UserService,
    private val refreshTokenStorage: RefreshTokenStorage,
    private val tokenGenerator: TokenGenerator,
) {
    fun refreshToken(refreshToken: RefreshToken): TokenResponse {
        val userPublicId = refreshTokenStorage.findAndRevoke(refreshToken)
            ?: throw InvalidRefreshTokenException("Invalid refresh token")

        val user = userService.findByPublicId(userPublicId)
        val accessTokenResponse = tokenGenerator.generateAccessToken(user.publicId)
        val refreshTokenResponse = tokenGenerator.generateRefreshToken()

        return TokenResponse(
            accessToken = accessTokenResponse.accessToken,
            refreshToken = refreshTokenResponse.refreshToken,
            accessTokenExpiresIn = accessTokenResponse.expiresIn,
            refreshTokenExpiresIn = refreshTokenResponse.expiresIn
        )
    }

}

