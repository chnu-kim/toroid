package me.chnu.toroid.application

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import me.chnu.toroid.domain.chzzk.ChannelId
import me.chnu.toroid.domain.chzzk.UserResponse
import me.chnu.toroid.domain.chzzk.auth.ChzzkAuthService
import me.chnu.toroid.domain.user.AccessToken
import me.chnu.toroid.domain.user.AccessTokenResponse
import me.chnu.toroid.domain.user.RefreshToken
import me.chnu.toroid.domain.user.RefreshTokenResponse
import me.chnu.toroid.domain.user.RefreshTokenStorage
import me.chnu.toroid.domain.user.SocialAccountProvider
import me.chnu.toroid.domain.user.TokenGenerator
import me.chnu.toroid.domain.user.User
import me.chnu.toroid.domain.user.UserPublicId
import me.chnu.toroid.domain.user.UserService
import java.net.URI
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

class ChzzkOAuthUseCaseTest : BehaviorSpec({

    val chzzkAuthService = mockk<ChzzkAuthService>()
    val userService = mockk<UserService>()
    val refreshTokenStorage = mockk<RefreshTokenStorage>()
    val tokenGenerator = mockk<TokenGenerator>()
    val sut = ChzzkOAuthUseCase(chzzkAuthService, userService, refreshTokenStorage, tokenGenerator)

    Given("유효한 code와 state가 주어졌을 때") {
        val code = "valid-code"
        val state = "valid-state"
        val channelId = ChannelId("channel-123")
        val publicId = UserPublicId.new()
        val user = User(id = 1L, name = "testUser", publicId = publicId)
        val chzzkUser = UserResponse(channelId = channelId, channelName = "testUser")
        val accessToken = AccessToken("access-token")
        val refreshToken = RefreshToken("refresh-token")
        val accessTokenResponse = AccessTokenResponse(accessToken, 1.hours)
        val refreshTokenResponse = RefreshTokenResponse(refreshToken, 30.days)

        every { chzzkAuthService.authenticate(code, state) } returns chzzkUser
        every {
            userService.saveOrUpdate(SocialAccountProvider.CHZZK, channelId.value, "testUser")
        } returns user
        every { tokenGenerator.generateAccessToken(publicId) } returns accessTokenResponse
        every { tokenGenerator.generateRefreshToken() } returns refreshTokenResponse
        justRun { refreshTokenStorage.save(refreshToken, publicId) }

        When("loadUser를 호출하면") {
            val result = sut.loadUser(code, state)

            Then("액세스 토큰을 반환한다") {
                result.accessToken shouldBe accessToken
            }

            Then("리프레시 토큰을 반환한다") {
                result.refreshToken shouldBe refreshToken
            }

            Then("리프레시 토큰을 저장한다") {
                verify { refreshTokenStorage.save(refreshToken, publicId) }
            }
        }
    }

    Given("빈 code가 주어졌을 때") {
        When("loadUser를 호출하면") {
            Then("InvalidOAuthParameterException을 발생시킨다") {
                shouldThrow<InvalidOAuthParameterException> {
                    sut.loadUser("", "valid-state")
                }
            }
        }
    }

    Given("빈 state가 주어졌을 때") {
        When("loadUser를 호출하면") {
            Then("InvalidOAuthParameterException을 발생시킨다") {
                shouldThrow<InvalidOAuthParameterException> {
                    sut.loadUser("valid-code", "")
                }
            }
        }
    }

    Given("code 길이가 512자를 초과할 때") {
        When("loadUser를 호출하면") {
            Then("InvalidOAuthParameterException을 발생시킨다") {
                shouldThrow<InvalidOAuthParameterException> {
                    sut.loadUser("a".repeat(513), "valid-state")
                }
            }
        }
    }

    Given("state 길이가 256자를 초과할 때") {
        When("loadUser를 호출하면") {
            Then("InvalidOAuthParameterException을 발생시킨다") {
                shouldThrow<InvalidOAuthParameterException> {
                    sut.loadUser("valid-code", "a".repeat(257))
                }
            }
        }
    }

    Given("getAuthUri를 호출할 때") {
        val expectedUri = URI.create("https://chzzk.naver.com/oauth/authorize?state=abc")
        every { chzzkAuthService.getAuthUri() } returns expectedUri

        When("인증 URI를 요청하면") {
            val result = sut.getAuthUri()

            Then("ChzzkAuthService의 URI를 반환한다") {
                result shouldBe expectedUri
            }
        }
    }
})
