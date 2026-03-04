# 실시간 이벤트 (Events)


> Socket.IO를 통해 수신되는 실시간 이벤트(채팅, 도네이션, 구독)의 메시지 스키마입니다.

---


## 시스템 메시지


세션 연결 및 구독 상태 변경 시 수신되는 메시지입니다.

| 이벤트            | 설명              | 주요 필드                        |
|----------------|-----------------|------------------------------|
| `connected`    | WebSocket 연결 성공 | `sessionKey`                 |
| `subscribed`   | 이벤트 구독 성공       | `eventType`, `channelId`     |
| `unsubscribed` | 이벤트 구독 해제       | `eventType`, `channelId`     |
| `revoked`      | 구독 자동 취소        | `eventType`, `channelId`, 사유 |

> `revoked`는 사용자 동의 철회, 스코프 변경, 인가 취소 시 발생합니다.

---


## CHAT 이벤트


채팅 메시지 수신 시 전달되는 이벤트입니다.


### 메시지 스키마

| 필드                     | 타입         | 설명                |
|------------------------|------------|-------------------|
| `senderChannelId`      | `String`   | 발신자 채널 식별자        |
| `profile`              | `Object`   | 발신자 프로필 정보        |
| `profile.nickname`     | `String`   | 닉네임               |
| `profile.badges`       | `Object[]` | 뱃지 목록             |
| `profile.verifiedMark` | `Boolean`  | 인증 마크 여부          |
| `profile.userRoleCode` | `String`   | 사용자 역할 코드 (아래 참조) |
| `content`              | `String`   | 메시지 내용            |
| `emojis`               | `Object`   | 이모지 데이터 (사용 시)    |
| `messageTime`          | `String`   | 메시지 전송 시간         |

### 예시

```json
{
  "senderChannelId": "abc123...",
  "profile": {
    "nickname": "치지직유저",
    "badges": [],
    "verifiedMark": false,
    "userRoleCode": "common_user"
  },
  "content": "안녕하세요!",
  "emojis": {},
  "messageTime": "2025-03-01T12:00:00Z"
}
```

---


## DONATION 이벤트


도네이션(후원) 발생 시 전달되는 이벤트입니다.


### 메시지 스키마

| 필드                 | 타입       | 설명                        |
|--------------------|----------|---------------------------|
| `donatorChannelId` | `String` | 후원자 채널 식별자                |
| `profile`          | `Object` | 후원자 프로필 정보                |
| `donationType`     | `String` | 후원 타입 (`CHAT` 또는 `VIDEO`) |
| `payAmount`        | `Int`    | 후원 금액                     |
| `donationText`     | `String` | 후원 메시지                    |
| `emojis`           | `Object` | 이모지 데이터 (사용 시)            |

### `donationType` 값

| 값       | 설명      |
|---------|---------|
| `CHAT`  | 채팅 도네이션 |
| `VIDEO` | 영상 도네이션 |

---


## SUBSCRIPTION 이벤트


구독 발생 시 전달되는 이벤트입니다.


### 메시지 스키마

| 필드                    | 타입       | 설명             |
|-----------------------|----------|----------------|
| `subscriberChannelId` | `String` | 구독자 채널 식별자     |
| `profile`             | `Object` | 구독자 프로필 정보     |
| `profile.nickname`    | `String` | 구독자 닉네임        |
| `tierNo`              | `Int`    | 구독 티어 (1 또는 2) |
| `tierName`            | `String` | 티어/브랜드 이름      |
| `month`               | `Int`    | 구독 개월 수        |

---


## `userRoleCode` 정의


Socket.IO 이벤트에서 사용자 역할을 구분하는 코드입니다.

| 코드                          | 설명            |
|-----------------------------|---------------|
| `streaming_channel_owner`   | 채널 소유자 (스트리머) |
| `streaming_channel_manager` | 채널 매니저        |
| `streaming_chat_manager`    | 채팅 매니저        |
| `common_user`               | 일반 사용자        |

---


## 코드베이스 매핑


프로젝트에서 이벤트를 처리하는 코드는 다음 파일을 참조하세요.

| 이벤트  | Socket.IO 이벤트명 | 처리 클래스                                         |
|------|----------------|------------------------------------------------|
| 채팅   | `CHAT`         | `SocketChzzkSessionManager` → `EventPublisher` |
| 도네이션 | `DONATION`     | `SocketChzzkSessionManager` → `EventPublisher` |
| 구독   | `SUBSCRIPTION` | `SocketChzzkSessionManager` → `EventPublisher` |

이벤트는 Redis Streams를 통해 `RedisStreamEventPublisher`로 발행됩니다.
