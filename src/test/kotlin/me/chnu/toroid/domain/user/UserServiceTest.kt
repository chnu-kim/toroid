package me.chnu.toroid.domain.user

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class UserServiceTest : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerTest

    val userRepository = mockk<UserRepository>()
    val socialAccountRepository = mockk<SocialAccountRepository>()
    val sut = UserService(userRepository, socialAccountRepository)

    Given("기존 사용자가 존재할 때") {
        val publicId = UserPublicId.new()
        val existingUser = User(id = 1L, name = "oldName", publicId = publicId)

        every {
            userRepository.findByProviderAndProviderId(SocialAccountProvider.CHZZK, "provider-123")
        } returns existingUser
        every { userRepository.save(any()) } returns existingUser

        When("saveOrUpdate를 호출하면") {
            val result = sut.saveOrUpdate(SocialAccountProvider.CHZZK, "provider-123", "newName")

            Then("기존 사용자의 이름을 변경한다") {
                result.name shouldBe "newName"
            }

            Then("사용자를 저장한다") {
                verify { userRepository.save(existingUser) }
            }

            Then("소셜 계정을 새로 생성하지 않는다") {
                verify(exactly = 0) { socialAccountRepository.save(any()) }
            }
        }
    }

    Given("기존 사용자가 존재하지 않을 때") {
        val newUser = User(id = 2L, name = "newUser", publicId = UserPublicId.new())

        every {
            userRepository.findByProviderAndProviderId(SocialAccountProvider.CHZZK, "new-provider-id")
        } returns null
        every { userRepository.save(any<User>()) } returns newUser
        every { socialAccountRepository.save(any<SocialAccount>()) } returns mockk()

        When("saveOrUpdate를 호출하면") {
            val result = sut.saveOrUpdate(SocialAccountProvider.CHZZK, "new-provider-id", "newUser")

            Then("새 사용자를 반환한다") {
                result shouldBe newUser
            }

            Then("소셜 계정을 저장한다") {
                verify { socialAccountRepository.save(any<SocialAccount>()) }
            }
        }
    }

    Given("publicId로 사용자를 조회할 때") {
        val publicId = UserPublicId.new()
        val user = User(id = 3L, name = "testUser", publicId = publicId)

        When("사용자가 존재하면") {
            every { userRepository.findByPublicId(publicId) } returns user

            Then("사용자를 반환한다") {
                val result = sut.findByPublicId(publicId)
                result shouldBe user
            }
        }

        When("사용자가 존재하지 않으면") {
            val unknownPublicId = UserPublicId.new()
            every { userRepository.findByPublicId(unknownPublicId) } returns null

            Then("UserNotFoundException을 발생시킨다") {
                shouldThrow<UserNotFoundException> {
                    sut.findByPublicId(unknownPublicId)
                }
            }
        }
    }
})
