# 세션 (Session)


> 실시간 이벤트 수신을 위한 세션 생성, 조회, 이벤트 구독/해제, Socket.IO 연결 API입니다.

---


## 개요


Session API는 Socket.IO(버전 1.0.0 ~ 2.0.3) WebSocket 연결을 통해 실시간 이벤트를 수신할 수 있게 합니다.


### 연결 제한

| 인증 방식             | 최대 동시 연결 |
|-------------------|----------|
| Client 인증         | 10개      |
| User Access Token | 3개       |

- 세션당 최대 **30개** 이벤트 구독 가능
- 지원 이벤트: `CHAT`, `DONATION`, `SUBSCRIPTION`

---


## 세션 생성 (Client 인증)


Client 인증 기반으로 세션을 생성합니다.

```http
GET /open/v1/sessions/auth/client
Client-Id: {clientId}
Client-Secret: {clientSecret}
```

**인증**: Client 인증


### 응답


WebSocket 연결 URL을 반환합니다. URL은 제한된 유효 기간을 가집니다.

---


## 세션 생성 (User 인증)


User Access Token 기반으로 세션을 생성합니다.

```http
GET /open/v1/sessions/auth
Authorization: Bearer {accessToken}
```

**인증**: Access Token (사용자 인증 필수)


### 응답


WebSocket 연결 URL을 반환합니다. 해당 Access Token의 권한에 맞는 이벤트만 수신됩니다.

---


## 세션 목록 조회 (Client)


Client 인증으로 생성된 세션 목록을 조회합니다.

```http
GET /open/v1/sessions/client?page=0&size=20
Client-Id: {clientId}
Client-Secret: {clientSecret}
```

**인증**: Client 인증


### 요청 파라미터

| 파라미터   | 타입    | 필수 | 기본값  | 설명          |
|--------|-------|----|------|-------------|
| `page` | `Int` | No | `0`  | 페이지 번호      |
| `size` | `Int` | No | `20` | 결과 수 (1~50) |

> 연결 해제된 세션은 90일간 조회 가능합니다.

---


## 세션 목록 조회 (User)


User Access Token으로 생성된 세션 목록을 조회합니다.

```http
GET /open/v1/sessions?page=0&size=20
Authorization: Bearer {accessToken}
```

**인증**: Access Token


### 요청 파라미터


세션 목록 조회 (Client)와 동일합니다.


### 응답

| 필드                 | 타입                    | 설명           |
|--------------------|-----------------------|--------------|
| `sessionKey`       | `String`              | 세션 키         |
| `connectedDate`    | `OffsetDateTime`      | 연결 시간        |
| `disconnectedDate` | `OffsetDateTime`      | 연결 해제 시간     |
| `subscribedEvents` | `ChzzkSessionEvent[]` | 구독 중인 이벤트 목록 |
| `channelId`        | `String`              | 채널 식별자       |

---


## 이벤트 구독


세션에 특정 이벤트를 구독합니다. 모든 구독 엔드포인트는 `sessionKey` 쿼리 파라미터가 필요합니다.


### 채팅 이벤트 구독

```http
POST /open/v1/sessions/events/subscribe/chat?sessionKey={sessionKey}
Authorization: Bearer {accessToken}
```

**스코프**: 채팅 메시지 조회


### 도네이션 이벤트 구독

```http
POST /open/v1/sessions/events/subscribe/donation?sessionKey={sessionKey}
Authorization: Bearer {accessToken}
```

**스코프**: 도네이션 조회


### 구독 이벤트 구독

```http
POST /open/v1/sessions/events/subscribe/subscription?sessionKey={sessionKey}
Authorization: Bearer {accessToken}
```

---


## 이벤트 구독 해제


### 채팅 이벤트 해제

```http
POST /open/v1/sessions/events/unsubscribe/chat?sessionKey={sessionKey}
Authorization: Bearer {accessToken}
```

### 도네이션 이벤트 해제

```http
POST /open/v1/sessions/events/unsubscribe/donation?sessionKey={sessionKey}
Authorization: Bearer {accessToken}
```

### 구독 이벤트 해제

```http
POST /open/v1/sessions/events/unsubscribe/subscription?sessionKey={sessionKey}
Authorization: Bearer {accessToken}
```

---


## Socket.IO 연결


세션 생성 시 반환된 WebSocket URL로 Socket.IO 연결을 수립합니다.

```javascript
const socket = io(sessionUrl, {
  reconnection: true,
  timeout: 3000,
  forceNew: true,
  transports: ["websocket"]
});
```

### 지원 버전

- Socket.IO **1.0.0 ~ 2.0.3**

### 시스템 메시지


연결 후 다음 시스템 메시지를 수신합니다.

| 메시지            | 설명                                     |
|----------------|----------------------------------------|
| `connected`    | 연결 성공. `sessionKey`를 제공                |
| `subscribed`   | 이벤트 구독 성공. `eventType`, `channelId` 반환 |
| `unsubscribed` | 이벤트 구독 해제 확인                           |
| `revoked`      | 구독 자동 취소 (사용자 동의 철회, 스코프 변경, 인가 취소 시)  |

이벤트 메시지 스키마는 [08-events.md](./08-events.md)를 참조하세요.
