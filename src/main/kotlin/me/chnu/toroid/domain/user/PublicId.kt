package me.chnu.toroid.domain.user

import com.fasterxml.jackson.annotation.JsonValue
import java.util.*

/**
 * Public identifier for a user.
 *
 * This value is used as the Spring Security authentication principal, so it can be injected via
 * `@AuthenticationPrincipal` in controller methods.
 */
@JvmInline
value class PublicId(@get:JsonValue val value: UUID) {

    companion object {
        fun new(): PublicId = PublicId(UUID.randomUUID())
    }

    override fun toString(): String = value.toString()
}
