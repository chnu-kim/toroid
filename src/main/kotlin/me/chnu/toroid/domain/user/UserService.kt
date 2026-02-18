package me.chnu.toroid.domain.user

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Transactional(readOnly = true)
@Service
class UserService(
    private val userRepository: UserRepository,
    private val socialAccountRepository: SocialAccountRepository,
) {
    @Transactional(readOnly = false)
    fun saveOrUpdate(provider: SocialAccountProvider, providerId: String, name: String): User {
        val user = userRepository.findByProviderAndProviderId(provider, providerId)
        if (user != null) {
            user.changeName(name)
            userRepository.save(user)
            return user
        }

        val newUser = userRepository.save(
            User(
                name = name,
                publicId = PublicId.new(),
                createdAt = OffsetDateTime.now(),
            )
        )
        socialAccountRepository.save(
            SocialAccount(
                provider = provider,
                providerId = providerId,
                user = newUser,
                createdAt = OffsetDateTime.now(),
            )
        )

        return newUser
    }

    fun findByPublicId(publicId: PublicId): User {
        return userRepository.findByPublicId(publicId)
            ?: throw UserNotFoundException("User not found")
    }
}

