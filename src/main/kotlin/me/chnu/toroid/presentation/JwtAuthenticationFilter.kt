package me.chnu.toroid.presentation

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import me.chnu.toroid.domain.user.AccessToken
import me.chnu.toroid.domain.user.PublicId
import me.chnu.toroid.domain.user.TokenValidator
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val tokenValidator: TokenValidator
) : OncePerRequestFilter() {

    private val logger = KotlinLogging.logger { }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            val accessToken = request.extractAccessToken()
            val userId = tokenValidator.extractUserId(accessToken)
            val token = AuthenticatedUserToken(userId)
            SecurityContextHolder.getContext().authentication = token
        } catch (e: Exception) {
            SecurityContextHolder.clearContext()
        } finally {
            filterChain.doFilter(request, response)
        }
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


    class AuthenticatedUserToken(private val userId: PublicId) : AbstractAuthenticationToken(listOf()) {
        init {
            super.isAuthenticated = true
        }

        override fun getCredentials() = null

        override fun getPrincipal() = UserPrincipal(userId)

    }
}