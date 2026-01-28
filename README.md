# chzzk-api-sample

Spring Boot (Kotlin) API that integrates Chzzk OAuth, issues JWTs, and stores user/session data with PostgreSQL + Redis.

## Features
- Chzzk OAuth login flow and user profile fetch
- JWT access/refresh token issuance (RSA)
- Refresh token storage in Redis
- User profile endpoint secured by JWT

## Requirements
- JDK 21
- Docker (for PostgreSQL/Redis via Compose)
- Chzzk API credentials

## Configuration
Set these values in `src/main/resources/application.yml` or environment variables:

- `app.public-base-url` (e.g., `http://127.0.0.1:8888`)
- `chzzk.client-id`
- `chzzk.client-secret`
- `chzzk.login-redirect-url-path` (e.g., `/chzzk/callback`)
- `chzzk.base-url` (e.g., `https://openapi.chzzk.naver.com`)
- `jwt.issuer`
- `jwt.public-key` and `jwt.private-key` (replace the sample keys in `src/main/resources/keys`)
- `spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password`
- `spring.data.redis.host`, `spring.data.redis.port`

`compose.yml` provides PostgreSQL and Redis for local development.

## Run
```bash
./gradlew bootRun
```

## API
- `GET /chzzk/authentication` -> redirects to Chzzk authorization page
- `GET {chzzk.login-redirect-url-path}` -> handles OAuth callback and returns tokens
- `GET /users/me` -> returns current user (requires `Authorization: Bearer <accessToken>`)

A sample request is in `http/auth.http`.

## Tests
```bash
./gradlew test
```
