package me.chnu.toroid.domain.user

import org.springframework.data.jpa.repository.JpaRepository

interface SocialAccountRepository : JpaRepository<SocialAccount, Long> {
    fun findByProviderAndProviderId(provider: SocialAccountProvider, providerId: String): SocialAccount?
}
