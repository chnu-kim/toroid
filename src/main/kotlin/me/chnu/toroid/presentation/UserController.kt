package me.chnu.toroid.presentation

import me.chnu.toroid.domain.user.PublicId
import me.chnu.toroid.domain.user.User
import me.chnu.toroid.domain.user.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService,
) {

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/users/me")
    fun getUser(
        @AuthenticationPrincipal publicId: PublicId,
    ): ResponseEntity<User> = userService
        .findByPublicId(publicId)
        .let { ResponseEntity.ok(it) }

}