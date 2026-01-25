package me.chnu.toroid.config.security

import me.chnu.toroid.presentation.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@EnableMethodSecurity
@Configuration
class SecurityConfig {

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(
        httpSecurity: HttpSecurity,
        jwtAuthenticationFilter: JwtAuthenticationFilter
    ): SecurityFilterChain {
        return httpSecurity
            .csrf { csrf: CsrfConfigurer<HttpSecurity> -> csrf.disable() }
            .formLogin { it.disable() }
            .anonymous { it.disable() }
            .httpBasic { it.disable() }
            .cors(Customizer.withDefaults())
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration().apply {
            allowedHeaders = listOf(
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT,
            )
            setAllowedMethods(listOf("GET", "POST", "PUT", "DELETE", "PATCH"))
            setAllowedOriginPatterns(listOf("*"))
            allowCredentials = true
            maxAge = 3600
        }

        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", config)
        }
    }
}