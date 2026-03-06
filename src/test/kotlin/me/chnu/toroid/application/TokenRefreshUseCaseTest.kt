package me.chnu.toroid.application

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import me.chnu.toroid.domain.user.AccessToken
import me.chnu.toroid.domain.user.AccessTokenResponse
import me.chnu.toroid.domain.user.RefreshToken
import me.chnu.toroid.domain.user.RefreshTokenResponse
import me.chnu.toroid.domain.user.RefreshTokenStorage
import me.chnu.toroid.domain.user.TokenGenerator
import me.chnu.toroid.domain.user.User
import me.chnu.toroid.domain.user.UserPublicId
import me.chnu.toroid.domain.user.UserService
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

class TokenRefreshUseCaseTest : BehaviorSpec({

    val userService = mockk<UserService>()
    val refreshTokenStorage = mockk<RefreshTokenStorage>()
    val tokenGenerator = mockk<TokenGenerator>()
    val sut = TokenRefreshUseCase(userService, refreshTokenStorage, tokenGenerator)

    Given("유효한 리프레시 토큰이 주어졌을 때") {
        val oldRefreshToken = RefreshToken("old-refresh-token")
        val publicId = UserPublicId.new()
        val user = User(id = 1L, name = "testUser", publicId = publicId)
        val newAccessToken = AccessToken("new-access-token")
        val newRefreshToken = RefreshToken("new-refresh-token")
        val accessTokenResponse = AccessTokenResponse(newAccessToken, 1.hours)
        val refreshTokenResponse = RefreshTokenResponse(newRefreshToken, 30.days)

        every { refreshTokenStorage.findAndRevoke(oldRefreshToken) } returns publicId
        every { userService.findByPublicId(publicId) } returns user
        every { tokenGenerator.generateAccessToken(publicId) } returns accessTokenResponse
        every { tokenGenerator.generateRefreshToken() } returns refreshTokenResponse
        justRun { refreshTokenStorage.save(newRefreshToken, publicId) }

        When("refreshToken을 호출하면") {
            val result = sut.refreshToken(oldRefreshToken)

            Then("새 액세스 토큰을 반환한다") {
                result.accessToken shouldBe newAccessToken
            }

            Then("새 리프레시 토큰을 반환한다") {
                result.refreshToken shouldBe newRefreshToken
            }

            Then("기존 리프레시 토큰을 폐기하고 새 토큰을 저장한다") {
                verify { refreshTokenStorage.findAndRevoke(oldRefreshToken) }
                verify { refreshTokenStorage.save(newRefreshToken, publicId) }
            }
        }
    }

    Given("유효하지 않은 리프레시 토큰이 주어졌을 때") {
        val invalidToken = RefreshToken("invalid-token")

        every { refreshTokenStorage.findAndRevoke(invalidToken) } returns null

        When("refreshToken을 호출하면") {
            Then("InvalidRefreshTokenException을 발생시킨다") {
                shouldThrow<InvalidRefreshTokenException> {
                    sut.refreshToken(invalidToken)
                }
            }
        }
    }
})
