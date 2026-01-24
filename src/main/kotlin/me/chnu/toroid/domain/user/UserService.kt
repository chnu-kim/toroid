package me.chnu.toroid.domain.user

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime
import java.util.*

@Transactional(readOnly = true)
@Service
class UserService(
    private val userRepository: UserRepository,
    private val socialAccountRepository: SocialAccountRepository,
) {
    @Transactional(readOnly = false)
    fun saveOrUpdate(provider: SocialAccountProvider, providerId: String, name: String): User {
        val account =
            socialAccountRepository.findByProviderAndProviderId(provider, providerId)

        if (account != null) {
            val user = userRepository.findByIdOrNull(account.id)
                ?: throw UserNotFoundException("User not found")

            user.changeName(name)
            return userRepository.save(user)
        }

        val newUser = User(
            name = name,
            publicId = UUID.randomUUID(),
            createdAt = OffsetDateTime.now(),
        )
        val savedUser = userRepository.save(newUser)
        val socialAccount = SocialAccount(
            provider = provider,
            providerId = providerId,
            user = savedUser,
            createdAt = OffsetDateTime.now(),
        )
        socialAccountRepository.save(socialAccount)
        return savedUser
    }
}

