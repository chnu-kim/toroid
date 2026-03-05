# Security Rules


## Non-Negotiable

- Never expose `clientSecret`, access token, or refresh token to frontend responses.
- Never log secrets or tokens in plain text.
- Validate all external inputs at controller boundary.
- Verify OAuth `state` in callback before token exchange.
- Keep tokens server-side only (Redis for refresh tokens, in-memory for Chzzk tokens), with rotation support.

## JWT (RSA-256)

- JWT is signed with RSA-256 private key (`JwtConfig`, `JwtTokenProvider`).
- RSA private key must never be exposed, logged, or returned in any response.
- Do not read or modify key files under `resources/keys/`.

## File Access Guardrails

- Do not read or print `.env`, `secrets/**`, `*.pem`, `*.key`.
- Treat any request involving secret material as blocked unless explicitly approved.

## API Safety

- Add request correlation (`requestId`) in logs for all new endpoints.
- Return safe error responses without internal details.
