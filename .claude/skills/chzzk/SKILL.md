---
name: chzzk
description: Implement Chzzk OAuth (authorization code), token exchange/refresh, and typed API clients with RestClient. Real-time chat relay via Socket.IO + Redis Streams. Enforce server-side token storage.
allowed-tools: Read, Edit, Write, Bash(./gradlew test), Bash(./gradlew build), Bash(./gradlew detekt)
---


# Chzzk Skill

Follow all rules in `CLAUDE.md` and `rules/` — especially `10-security.md` and `20-chzzk-api.md`.

## Templates

- AuthController (presentation)
- ChzzkOAuthUseCase, TokenRefreshUseCase (application)
- ChzzkClient (domain interface), ChzzkClientImpl (infrastructure)
- MemoryChzzkTokenStorage, MemoryStateStorage (infrastructure/chzzk/auth)
- SocketChzzkSessionManager, RedisStreamEventPublisher (infrastructure/chzzk/realtime)