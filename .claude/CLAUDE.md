# Project: Chzzk Integration (Toroid)


## Goal

- Integrate Chzzk OAuth + API calls + real-time chat events
- Never expose client secret / refresh token to frontend
- Keep tokens server-side (Redis for refresh tokens, in-memory for Chzzk tokens), rotate when needed

## Tech

- Kotlin + Spring Boot
- RestClient (Apache HttpComponents5) for HTTP
- Redis for refresh token storage + event streaming (Redis Streams)
- PostgreSQL for user/social account persistence
- JWT (RSA-256) for authentication
- Socket.IO for real-time Chzzk chat relay

## Commands

- Build: `./gradlew build`
- Test: `./gradlew test`
- Lint: `./gradlew detekt`

## Rules 준수 (non-negotiable)

- `.claude/rules/`에 path-scoped rule이 로드되면, 해당 rule이 `@docs/` 구문으로 참조하는 문서를 **작업 전에 반드시 Read 도구로 읽어야** 한다.
- 문서를 읽지 않고 추측으로 KDoc, 주석, 구현 코드를 작성하는 것을 금지한다.

## Security

- See `rules/10-security.md` for full security rules.
- Do not read or print `.env`, `secrets/**`, `*.key`, `*.pem`.
- RSA private key must never be exposed or logged.

## Architecture

- Domain layer must NOT depend on infrastructure or presentation. Konsist enforces this (see `ArchitectureTest.kt`).
- Application layer orchestrates domain interfaces; infrastructure provides implementations.
- Kotest is used as the test framework.