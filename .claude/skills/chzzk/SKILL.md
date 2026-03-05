---
name: chzzk
description: Implement Chzzk OAuth (authorization code), token exchange/refresh, and typed API clients with RestClient. Real-time chat relay via Socket.IO + Redis Streams. Enforce server-side token storage.
allowed-tools: Read, Edit, Write, Bash(./gradlew test), Bash(./gradlew build), Bash(./gradlew detekt)
---


# Chzzk Skill


## Rules

- Never return refresh token to frontend.
- Verify OAuth state.
- Chzzk API calls via RestClient in infrastructure layer (`ChzzkClientImpl`).
- Domain interfaces in `domain/chzzk/`, implementations in `infrastructure/chzzk/`.
- Add integration tests for OAuth callback and token refresh.
- Use Kotest for tests, Konsist for architecture validation.

## Templates

- AuthController (presentation)
- ChzzkOAuthUseCase, TokenRefreshUseCase (application)
- ChzzkClient (domain interface), ChzzkClientImpl (infrastructure)
- MemoryChzzkTokenStorage, MemoryStateStorage (infrastructure/chzzk/auth)
- SocketChzzkSessionManager, RedisStreamEventPublisher (infrastructure/chzzk/realtime)
