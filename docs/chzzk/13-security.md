# 보안 가이드라인 (Security)


> 토큰 관리, CSRF 방어, 앱 정책 등 Chzzk API 사용 시 준수해야 할 보안 사항을 정리한 문서입니다.

---


## 토큰 기밀 유지

- `clientSecret`, Access Token, Refresh Token은 **절대로** 프론트엔드 응답에 포함하지 않습니다.
- 토큰을 로그에 평문으로 기록하지 않습니다.
- 토큰을 소스 코드, 공개 저장소에 저장하지 않습니다.
- 서버 사이드에서만 토큰을 관리합니다.
  - Refresh Token → Redis 저장
  - Chzzk Access Token → 메모리 저장

---


## CSRF 방어 (`state` 파라미터)


OAuth 인가 요청 시 `state` 파라미터를 반드시 사용합니다.

1. **서버에서** 랜덤 `state` 값을 생성합니다.
2. 인가 URL에 `state`를 포함하여 리다이렉트합니다.
3. 콜백에서 전달받은 `state`를 **서버에 저장된 값과 비교**합니다.
4. 불일치 시 요청을 거부합니다.

```kotlin
// 프로젝트 구현: domain/chzzk/auth/StateStorage
interface StateStorage {
    fun save(state: String)
    fun validate(state: String): Boolean
}
```

---


## Content-Type 헤더


모든 API 요청에 `Content-Type: application/json` 헤더를 필수로 포함합니다.

```http
Content-Type: application/json
Accept: application/json
```

---


## 앱 이름 규칙

| 규칙            | 설명                                         |
|---------------|--------------------------------------------|
| 공식 서비스명 사용 금지 | 앱 이름에 `chzzk`, `naver`, `치지직`, `네이버` 포함 불가 |
| 부적절한 표현 금지    | 비속어, 혐오 표현 등 사용 시 서비스 정지                   |

---


## 토큰 수명 관리

| 토큰            | TTL         | 특성                          |
|---------------|-------------|-----------------------------|
| Access Token  | 1일 (86400초) | 만료 시 `401 INVALID_TOKEN` 반환 |
| Refresh Token | 30일         | **일회용** — 사용 후 즉시 무효화       |

- Access Token 만료 전에 Refresh Token으로 갱신하는 로직을 구현합니다.
- Refresh Token 사용 시 새로운 Refresh Token이 발급됩니다.
- 토큰 폐기(`/auth/v1/token/revoke`)는 동일 `clientId`+사용자 조합의 모든 토큰을 무효화합니다.

---


## 프로젝트 보안 설정


이 프로젝트에서 적용하고 있는 보안 정책:

| 항목               | 구현                                              |
|------------------|-------------------------------------------------|
| JWT 서명           | RSA-256 (`JwtConfig`, `JwtTokenProvider`)       |
| Refresh Token 저장 | Redis (`RedisRefreshTokenStorage`)              |
| Chzzk Token 저장   | 메모리 (`MemoryChzzkTokenStorage`)                 |
| State 저장         | 메모리 (`MemoryStateStorage`)                      |
| 파일 접근 제한         | `.env`, `secrets/**`, `*.pem`, `*.key` 읽기/출력 금지 |
| RSA 키 보호         | 비공개 키 노출/로깅 금지                                  |
