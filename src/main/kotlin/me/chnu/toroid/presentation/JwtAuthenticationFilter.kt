package me.chnu.toroid.presentation

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import me.chnu.toroid.domain.user.AccessToken
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val accessToken = request.extractAccessToken()
        TODO("Not yet implemented")
    }

    private fun HttpServletRequest.extractAccessToken(): AccessToken {
        val authorizationHeader = getHeader("Authorization")
            ?: throw IllegalArgumentException("Authorization header is missing")

        if (!authorizationHeader.startsWith("Bearer ")) {
            throw IllegalArgumentException("Invalid Authorization header format")
        }

        val token = authorizationHeader.substring(7)
        return AccessToken(token)
    }
}