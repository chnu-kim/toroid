package me.chnu.toroid.application

import me.chnu.toroid.domain.chzzk.auth.ChzzkAuthService
import me.chnu.toroid.domain.user.RefreshTokenStorage
import me.chnu.toroid.domain.user.SocialAccountProvider
import me.chnu.toroid.domain.user.TokenGenerator
import me.chnu.toroid.domain.user.UserService
import org.springframework.stereotype.Service

@Service
class ChzzkAuthUseCase(
    private val chzzkAuthService: ChzzkAuthService,
    private val userService: UserService,
    private val refreshTokenStorage: RefreshTokenStorage,
    private val tokenGenerator: TokenGenerator,
) {

    fun loadUser(code: String, state: String): TokenResponse {
        val chzzkUser = chzzkAuthService.authenticate(code, state)
        val user = userService.saveOrUpdate(
            SocialAccountProvider.CHZZK,
            chzzkUser.channelId.value,
            chzzkUser.channelName
        )

        val accessTokenResponse = tokenGenerator.generateAccessToken(user.publicId)
        val refreshTokenResponse = tokenGenerator.generateRefreshToken()

        refreshTokenStorage.save(refreshTokenResponse.refreshToken, user.publicId)

        return TokenResponse(
            accessTokenResponse.accessToken,
            refreshTokenResponse.refreshToken,
            accessTokenResponse.expiresIn,
            refreshTokenResponse.expiresIn,
        )
    }
}


