---
paths:
  - src/**/chzzk/**/*.kt
  - src/**/config/chzzk/**/*.kt
  - contract/http/**/*.kt
---

# Chzzk API Conventions


## OAuth Flow

- Use authorization code flow.
- `state` must be generated server-side and validated on callback.
- Exchange code for token server-side only.
- Refresh token must never be returned to frontend.

## Client Design

- Wrap Chzzk APIs in typed DTOs via `RestClient` (Apache HttpComponents5).
- Define `ChzzkClient` interface in domain layer (`domain/chzzk/`).
- Implement as `ChzzkClientImpl` in infrastructure layer (`infrastructure/chzzk/`).
- Convert external API errors into `ChzzkApiException` (domain-safe).

## Persistence

- Currently using in-memory implementations: `MemoryChzzkTokenStorage`, `MemoryStateStorage`.
- Domain interfaces (`ChzzkTokenStorage`, `StateStorage`) live in `domain/chzzk/auth/`.
- User refresh tokens are stored in Redis via `RedisRefreshTokenStorage`.
- User/social account relationships are persisted in PostgreSQL.

## Real-time Events

- Chzzk chat events are received via Socket.IO (`SocketChzzkSessionManager`, `ChatListener`).
- Events are published to Redis Streams via `RedisStreamEventPublisher`.
- Domain interfaces: `ChzzkEventListener`, `ChzzkSessionManager` in `domain/chzzk/realtime/`.
- Event types: chat messages, donations, subscriptions.

## Tests

- Add integration tests for OAuth callback success/failure (`state` mismatch).
- Add token refresh tests (success, invalid refresh token).
- Keep at least one typed endpoint contract test for Chzzk client.
- Use Kotest as the test framework.
- Konsist enforces architecture layer constraints (see `ArchitectureTest.kt`).