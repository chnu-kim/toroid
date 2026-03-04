# 인증 (Authentication)


> OAuth 2.0 인가 코드 플로우를 통한 토큰 발급, 갱신, 폐기 API를 정리한 문서입니다.

---


## 개요


Chzzk API는 **Authorization Code Grant** 방식의 OAuth 2.0을 사용합니다.

- **Access Token TTL**: 1일 (86400초)
- **Refresh Token TTL**: 30일 (일회용 — 갱신 시 새로운 토큰 발급)

---


## 인가 코드 요청


사용자를 Chzzk 로그인 페이지로 리다이렉트합니다.

```http
GET https://chzzk.naver.com/account-interlock?clientId={clientId}&redirectUri={redirectUri}&state={state}
```

### 요청 파라미터

| 파라미터          | 타입       | 필수  | 설명                            |
|---------------|----------|-----|-------------------------------|
| `clientId`    | `String` | Yes | 앱 식별자                         |
| `redirectUri` | `String` | Yes | 등록된 로그인 리다이렉트 URL과 정확히 일치해야 함 |
| `state`       | `String` | Yes | CSRF 방어용 토큰 (서버에서 생성)         |

### 리다이렉트 응답


인증 성공 시 `redirectUri`로 리다이렉트되며, 아래 파라미터가 전달됩니다.

| 파라미터    | 타입       | 설명                         |
|---------|----------|----------------------------|
| `code`  | `String` | 인가 코드                      |
| `state` | `String` | 요청 시 전달한 `state` 값 (검증 필수) |

---


## 토큰 교환 (Access Token 발급)


인가 코드를 Access Token으로 교환합니다.

```http
POST /auth/v1/token
Content-Type: application/json
```

### 요청 본문

| 파라미터           | 타입       | 필수  | 설명                        |
|----------------|----------|-----|---------------------------|
| `grantType`    | `String` | Yes | `authorization_code` (고정) |
| `clientId`     | `String` | Yes | 앱 식별자                     |
| `clientSecret` | `String` | Yes | 앱 시크릿                     |
| `code`         | `String` | Yes | 인가 코드                     |
| `state`        | `String` | Yes | state 파라미터 (검증용)          |

### 응답

| 필드             | 타입       | 설명                          |
|----------------|----------|-----------------------------|
| `accessToken`  | `String` | API 호출용 Bearer 토큰           |
| `refreshToken` | `String` | 일회용 갱신 토큰                   |
| `tokenType`    | `String` | `Bearer` (고정)               |
| `expiresIn`    | `String` | 만료까지 남은 시간(초). `86400` = 1일 |

```json
{
  "code": 200,
  "message": null,
  "content": {
    "accessToken": "eyJhbGciOi...",
    "refreshToken": "r1a2b3c4d5...",
    "tokenType": "Bearer",
    "expiresIn": "86400"
  }
}
```

---


## 토큰 갱신 (Refresh)


만료된 Access Token을 갱신합니다. Refresh Token은 **일회용**이므로, 갱신 시 이전 토큰은 무효화됩니다.

```http
POST /auth/v1/token
Content-Type: application/json
```

### 요청 본문

| 파라미터           | 타입       | 필수  | 설명                     |
|----------------|----------|-----|------------------------|
| `grantType`    | `String` | Yes | `refresh_token` (고정)   |
| `refreshToken` | `String` | Yes | 이전에 발급받은 Refresh Token |
| `clientId`     | `String` | Yes | 앱 식별자                  |
| `clientSecret` | `String` | Yes | 앱 시크릿                  |

### 응답

| 필드             | 타입       | 설명                      |
|----------------|----------|-------------------------|
| `accessToken`  | `String` | 새로운 Access Token        |
| `refreshToken` | `String` | 새로운 Refresh Token (일회용) |
| `tokenType`    | `String` | `Bearer`                |
| `expiresIn`    | `String` | 만료까지 남은 시간(초)           |
| `scope`        | `String` | 부여된 스코프                 |

---


## 토큰 폐기 (Revoke)


토큰을 명시적으로 폐기합니다. 동일한 `clientId`와 사용자 조합의 모든 토큰이 폐기됩니다.

```http
POST /auth/v1/token/revoke
Content-Type: application/json
```

### 요청 본문

| 파라미터            | 타입       | 필수  | 설명                                      |
|-----------------|----------|-----|-----------------------------------------|
| `clientId`      | `String` | Yes | 앱 식별자                                   |
| `clientSecret`  | `String` | Yes | 앱 시크릿                                   |
| `token`         | `String` | Yes | 폐기할 Access Token 또는 Refresh Token       |
| `tokenTypeHint` | `String` | No  | `access_token` (기본값) 또는 `refresh_token` |

---


## 인증 헤더


### Client 인증

```http
Client-Id: {clientId}
Client-Secret: {clientSecret}
```

### Bearer Token 인증

```http
Authorization: Bearer {accessToken}
```

---


## 주의사항

- Access Token 만료 시 API는 `401 (INVALID_TOKEN)`을 반환합니다.
- `state` 파라미터는 CSRF 방어를 위해 **반드시 서버에서 생성하고 콜백 시 검증**해야 합니다.
- `redirectUri`는 개발자 센터에 등록된 값과 정확히 일치해야 합니다.
- Refresh Token은 일회용이며, 사용 후 즉시 무효화됩니다.
