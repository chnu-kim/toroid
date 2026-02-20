package me.chnu.toroid.domain.user

import com.fasterxml.jackson.annotation.JsonValue
import java.util.*

/**
 * Public identifier for a user.
 */
@JvmInline
value class UserPublicId(@get:JsonValue val value: UUID) {

    companion object {
        fun new(): UserPublicId = UserPublicId(UUID.randomUUID())
    }

    override fun toString(): String = value.toString()
}
