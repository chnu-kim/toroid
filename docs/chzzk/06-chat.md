# 채팅 (Chat)


> 채팅 메시지 전송, 공지 등록, 채팅 설정 조회/수정 API입니다.

모든 Chat API는 **Access Token (사용자 인증)** 이 필요합니다.

---


## 채팅 메시지 전송


채널 채팅에 메시지를 전송합니다.

```http
POST /open/v1/chats/send
Authorization: Bearer {accessToken}
Content-Type: application/json
```

### 요청 본문

| 파라미터      | 타입       | 필수  | 설명               |
|-----------|----------|-----|------------------|
| `message` | `String` | Yes | 메시지 내용 (최대 100자) |

### 응답

| 필드          | 타입       | 설명          |
|-------------|----------|-------------|
| `messageId` | `String` | 전송된 메시지 식별자 |

---


## 채팅 공지 등록


새 메시지 또는 기존 메시지를 채팅 공지로 등록합니다.

```http
POST /open/v1/chats/notice
Authorization: Bearer {accessToken}
Content-Type: application/json
```

### 요청 본문

| 파라미터        | 타입       | 필수 | 설명                 |
|-------------|----------|----|--------------------|
| `message`   | `String` | No | 새 공지 메시지 (최대 100자) |
| `messageId` | `String` | No | 기존 메시지 ID를 공지로 지정  |

> `message` 또는 `messageId` 중 하나를 제공해야 합니다.


### 응답


성공 시 HTTP 200을 반환합니다.

---


## 채팅 설정 조회


채널의 채팅 설정을 조회합니다.

```http
GET /open/v1/chats/settings
Authorization: Bearer {accessToken}
```

### 요청 파라미터


없음


### 응답

| 필드                              | 타입        | 설명                           |
|---------------------------------|-----------|------------------------------|
| `chatAvailableCondition`        | `String`  | `NONE` 또는 `REAL_NAME`        |
| `chatAvailableGroup`            | `String`  | 채팅 허용 그룹 (아래 참조)             |
| `minFollowerMinute`             | `Int`     | `FOLLOWER` 모드에서 최소 팔로우 시간(분) |
| `allowSubscriberInFollowerMode` | `Boolean` | 구독자 최소 팔로우 시간 면제 여부          |

### `chatAvailableGroup` 값

| 값            | 설명     |
|--------------|--------|
| `ALL`        | 모든 사용자 |
| `FOLLOWER`   | 팔로워만   |
| `MANAGER`    | 매니저만   |
| `SUBSCRIBER` | 구독자만   |

---


## 채팅 설정 수정


채널의 채팅 설정을 수정합니다.

```http
PUT /open/v1/chats/settings
Authorization: Bearer {accessToken}
Content-Type: application/json
```

### 요청 본문

| 파라미터                            | 타입        | 필수 | 설명                                                                                  |
|---------------------------------|-----------|----|-------------------------------------------------------------------------------------|
| `chatAvailableCondition`        | `String`  | No | `NONE` 또는 `REAL_NAME`                                                               |
| `chatAvailableGroup`            | `String`  | No | `ALL`, `FOLLOWER`, `MANAGER`, `SUBSCRIBER`                                          |
| `minFollowerMinute`             | `Int`     | No | 허용 값: `0, 5, 10, 30, 60, 1440, 10080, 43200, 86400, 129600, 172800, 216000, 259200` |
| `allowSubscriberInFollowerMode` | `Boolean` | No | 구독자 면제 여부                                                                           |
| `chatSlowModeSec`               | `Int`     | No | 메시지 전송 간격(초). 허용 값: `0, 3, 5, 10, 30, 60, 120, 300`                                 |
| `chatEmojiMode`                 | `Boolean` | No | 이모지 전용 모드                                                                           |

### 응답


성공 시 HTTP 200을 반환합니다.
