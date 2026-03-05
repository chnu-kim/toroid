package me.chnu.toroid.application

import me.chnu.toroid.domain.chzzk.auth.ChzzkAuthService
import me.chnu.toroid.domain.user.RefreshTokenStorage
import me.chnu.toroid.domain.user.SocialAccountProvider
import me.chnu.toroid.domain.user.TokenGenerator
import me.chnu.toroid.domain.user.UserService
import org.springframework.stereotype.Service

@Service
class ChzzkOAuthUseCase(
    private val chzzkAuthService: ChzzkAuthService,
    private val userService: UserService,
    private val refreshTokenStorage: RefreshTokenStorage,
    private val tokenGenerator: TokenGenerator,
) {

    fun loadUser(code: String, state: String): TokenResponse {
        if (code.isBlank() || code.length > MAX_CODE_LENGTH) {
            throw InvalidOAuthParameterException("Invalid code parameter")
        }
        if (state.isBlank() || state.length > MAX_STATE_LENGTH) {
            throw InvalidOAuthParameterException("Invalid state parameter")
        }

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

    fun getAuthUri() = chzzkAuthService.getAuthUri()

    companion object {
        private const val MAX_CODE_LENGTH = 512
        private const val MAX_STATE_LENGTH = 256
    }
}
