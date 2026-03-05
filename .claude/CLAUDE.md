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

## Security rules (non-negotiable)

- Do not read or print .env, secrets/**, *.key, *.pem
- Any new endpoint must validate inputs and log with requestId
- OAuth state must be verified
- RSA private key must never be exposed or logged

## Where to implement what

- `presentation/`: Controllers (AuthController, UserController), filters (JwtAuthenticationFilter), exception handling (GlobalExceptionHandler)
- `application/`: Use cases (ChzzkOAuthUseCase, TokenRefreshUseCase)
- `domain/user/`: User, SocialAccount, token models, repository interfaces (UserRepository, RefreshTokenStorage)
- `domain/chzzk/`: ChzzkClient interface, DTOs, ChzzkApiException
- `domain/chzzk/auth/`: ChzzkOAuthInitiator, ChzzkTokenStorage, StateStorage, ChzzkAuthService
- `domain/chzzk/realtime/`: ChzzkEventListener, ChzzkSessionManager interfaces
- `domain/trpg/`: TRPG session models (TrpgSession, SessionStatus)
- `infrastructure/chzzk/`: ChzzkClientImpl, ChzzkResponse
- `infrastructure/chzzk/auth/`: ChzzkOAuthInitiatorImpl, MemoryChzzkTokenStorage, MemoryStateStorage
- `infrastructure/chzzk/realtime/`: SocketChzzkSessionManager, ChatListener, RedisStreamEventPublisher
- `infrastructure/user/`: JwtTokenProvider, RedisRefreshTokenStorage
- `config/`: JwtConfig, RedisConfig, SecurityConfig, ChzzkConfiguration
- `contract/http/`: ChzzkRoutes (HTTP contract definitions)

## Architecture

- Konsist-based architecture tests enforce layer dependency rules (see ArchitectureTest.kt)
- Domain layer must NOT depend on infrastructure or presentation
- Application layer orchestrates domain interfaces; infrastructure provides implementations
- Kotest is used as the test framework
