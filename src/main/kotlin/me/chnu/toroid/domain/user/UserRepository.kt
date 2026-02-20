package me.chnu.toroid.domain.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<User, Long> {
    fun findByPublicId(publicId: UserPublicId): User?

    @Query("SELECT sa.user FROM SocialAccount sa WHERE sa.provider = :provider AND sa.providerId = :providerId")
    fun findByProviderAndProviderId(provider: SocialAccountProvider, providerId: String): User?
}
