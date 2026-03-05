package me.chnu.toroid.domain.chzzk.auth

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import me.chnu.toroid.domain.chzzk.ChannelId
import me.chnu.toroid.domain.chzzk.ChzzkClient
import me.chnu.toroid.domain.chzzk.TokenResponse
import me.chnu.toroid.domain.chzzk.UserResponse
import java.net.URI

class ChzzkAuthServiceTest : BehaviorSpec({

    val chzzkClient = mockk<ChzzkClient>()
    val stateStorage = mockk<StateStorage>()
    val chzzkTokenStorage = mockk<ChzzkTokenStorage>()
    val chzzkOAuthInitiator = mockk<ChzzkOAuthInitiator>()
    val sut = ChzzkAuthService(chzzkClient, stateStorage, chzzkTokenStorage, chzzkOAuthInitiator)

    isolationMode = IsolationMode.InstancePerTest

    Given("getAuthUri를 호출할 때") {
        val expectedUri = URI.create("https://chzzk.naver.com/oauth/authorize?state=abc")
        every { chzzkOAuthInitiator.issueAuthUri() } returns AuthRequest(expectedUri, "abc")

        When("인증 URI를 요청하면") {
            val result = sut.getAuthUri()

            Then("OAuth 인증 URI를 반환한다") {
                result shouldBe expectedUri
            }
        }
    }

    Given("유효한 code와 state가 주어졌을 때") {
        val code = "valid-code"
        val state = "valid-state"
        val channelId = ChannelId("channel-123")
        val tokenResponse = TokenResponse(
            refreshToken = "refresh-token",
            accessToken = "access-token",
            tokenType = "Bearer",
            expiresIn = 3600L,
            scope = "read",
        )
        val userResponse = UserResponse(channelId = channelId, channelName = "testChannel")

        every { stateStorage.consumeState(state) } returns true
        every { chzzkClient.requestAccessToken(code, state) } returns tokenResponse
        every { chzzkClient.getUserInfo("access-token") } returns userResponse
        justRun { chzzkTokenStorage.storeAccessToken(channelId, any(), any()) }
        justRun { chzzkTokenStorage.storeRefreshToken(channelId, any(), any()) }

        When("authenticate를 호출하면") {
            val result = sut.authenticate(code, state)

            Then("사용자 정보를 반환한다") {
                result shouldBe userResponse
            }

            Then("액세스 토큰을 저장한다") {
                verify { chzzkTokenStorage.storeAccessToken(channelId, "access-token", any()) }
            }

            Then("리프레시 토큰을 저장한다") {
                verify { chzzkTokenStorage.storeRefreshToken(channelId, "refresh-token", any()) }
            }
        }
    }

    Given("유효하지 않은 state가 주어졌을 때") {
        every { stateStorage.consumeState("invalid-state") } returns false

        When("authenticate를 호출하면") {
            Then("예외를 발생시킨다") {
                shouldThrow<IllegalStateException> {
                    sut.authenticate("code", "invalid-state")
                }
            }
        }
    }
})
