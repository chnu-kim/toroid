package me.chnu.toroid.presentation

import me.chnu.toroid.domain.user.PublicId

/**
 * This value is used as the Spring Security authentication principal, so it can be injected via
 * `@AuthenticationPrincipal` in controller methods.
 */
data class UserPrincipal(
    val id: PublicId,
)
