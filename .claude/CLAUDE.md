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

# Project Context


## Claude Code


@docs/claudecode/claude-code-qa-session.md
@docs/claudecode/claude-directory-guide.md
