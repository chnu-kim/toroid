package me.chnu.toroid.domain.user

import java.util.UUID

interface TokenValidator {
    /**
     * 토큰의 유효성을 검증합니다.
     * - 서명 검증
     * - 만료 시간 검증
     * - 발급자(issuer) 검증
     *
     * @param token 검증할 액세스 토큰
     * @return 토큰이 유효하면 true, 그렇지 않으면 false
     */
    fun verifyToken(token: AccessToken): Boolean

    /**
     * 토큰에서 사용자 ID(subject)를 추출합니다.
     * 
     * 참고: 이 메서드는 토큰 검증을 수행하지 않습니다.
     * 호출 전에 verifyToken()으로 유효성을 먼저 확인하거나,
     * 검증과 추출을 함께 수행하는 경우에만 사용하세요.
     *
     * @param token 사용자 ID를 추출할 액세스 토큰
     * @return 토큰의 subject에 포함된 사용자 UUID
     * @throws com.auth0.jwt.exceptions.JWTVerificationException 토큰이 유효하지 않은 경우
     */
    fun extractUserId(token: AccessToken): UUID
}