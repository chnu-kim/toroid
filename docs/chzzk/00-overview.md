# Chzzk Open API 개요


> 치지직(Chzzk) Open API의 기본 정보, 인증 방식, 공통 응답 구조를 정리한 문서입니다.

---


## API 도메인

```
https://openapi.chzzk.naver.com
```

모든 API 요청에는 `Content-Type: application/json` 헤더가 필수입니다.

---


## 인증 방식


Chzzk API는 두 가지 인증 방식을 지원합니다.

| 인증 방식                 | 헤더                                                       | 사용 대상                               |
|-----------------------|----------------------------------------------------------|-------------------------------------|
| **Client 인증**         | `Client-Id: {clientId}`, `Client-Secret: {clientSecret}` | 앱 레벨 API (채널 조회, 카테고리 검색, 라이브 목록 등) |
| **User Access Token** | `Authorization: Bearer {accessToken}`                    | 사용자 레벨 API (내 정보, 채팅, 세션 등)         |

---


## 공통 응답 구조


### 성공 응답 (HTTP 200)

```json
{
  "code": 200,
  "message": null,
  "content": { /* 응답 데이터 */ }
}
```

### 실패 응답

```json
{
  "code": 401,
  "message": "INVALID_TOKEN"
}
```

| 필드        | 타입        | 설명                   |
|-----------|-----------|----------------------|
| `code`    | `Int`     | HTTP 상태 코드           |
| `message` | `String?` | 에러 메시지 (성공 시 `null`) |
| `content` | `T?`      | 응답 데이터 (실패 시 `null`) |

---


## API 카테고리

| 문서                                             | 설명                                |
|------------------------------------------------|-----------------------------------|
| [01-authentication.md](./01-authentication.md) | OAuth 인가 코드 플로우, 토큰 교환/갱신/폐기      |
| [02-user.md](./02-user.md)                     | 사용자 정보 조회                         |
| [03-channel.md](./03-channel.md)               | 채널 정보, 매니저, 팔로워, 구독자              |
| [04-category.md](./04-category.md)             | 카테고리 검색                           |
| [05-live.md](./05-live.md)                     | 라이브 목록, 스트림 키, 방송 설정              |
| [06-chat.md](./06-chat.md)                     | 채팅 메시지 전송, 공지, 설정                 |
| [07-session.md](./07-session.md)               | 세션 생성/관리, 이벤트 구독/해제, Socket.IO 연결 |
| [08-events.md](./08-events.md)                 | 실시간 이벤트(채팅, 도네이션, 구독) 메시지 스키마     |
| [09-drops.md](./09-drops.md)                   | 드롭스 리워드 조회/업데이트                   |
| [10-restriction.md](./10-restriction.md)       | 활동 제한 추가/삭제/조회                    |
| [11-error-handling.md](./11-error-handling.md) | HTTP 상태 코드, 에러 메시지                |
| [12-rate-limit.md](./12-rate-limit.md)         | Rate Limit, 429 처리                |
| [13-security.md](./13-security.md)             | 보안 가이드라인, 토큰 관리                   |

---


## 앱 정책

- 90일간 API Scope 미사용 시 앱이 자동 삭제됩니다.
- 앱 이름에 `chzzk`, `naver`(한국어 포함)를 포함할 수 없습니다.
- 부적절한 앱 이름은 서비스 정지 사유가 될 수 있습니다.

---


## 참고

- 공식 문서: https://chzzk.gitbook.io/chzzk
- 개발자 센터: https://developers.chzzk.naver.com
