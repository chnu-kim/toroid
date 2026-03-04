# 채널 (Channel)


> 채널 정보 조회, 매니저, 팔로워, 구독자 목록을 조회하는 API입니다.

---


## 채널 정보 조회


최대 20개의 채널 정보를 일괄 조회합니다.

```http
GET /open/v1/channels?channelIds={id1}&channelIds={id2}
Client-Id: {clientId}
Client-Secret: {clientSecret}
```

**인증**: Client 인증


### 요청 파라미터

| 파라미터         | 타입         | 필수  | 설명                   |
|--------------|------------|-----|----------------------|
| `channelIds` | `String[]` | Yes | 조회할 채널 ID 목록. 최대 20개 |

### 응답 (`data` 배열)

| 필드                | 타입        | 설명             |
|-------------------|-----------|----------------|
| `channelId`       | `String`  | 채널 식별자         |
| `channelName`     | `String`  | 채널 이름          |
| `channelImageUrl` | `String`  | 채널 프로필 이미지 URL |
| `followerCount`   | `Int`     | 팔로워 수          |
| `verifiedMark`    | `Boolean` | 인증 마크 여부       |

---


## 채널 매니저 조회 (Streaming Roles)


내 채널의 매니저 목록을 조회합니다.

```http
GET /open/v1/channels/streaming-roles
Authorization: Bearer {accessToken}
```

**인증**: Access Token (사용자 인증 필수)


### 요청 파라미터


없음


### 응답 (`data` 배열)

| 필드                   | 타입       | 설명          |
|----------------------|----------|-------------|
| `managerChannelId`   | `String` | 매니저의 채널 식별자 |
| `managerChannelName` | `String` | 매니저의 채널 이름  |
| `userRole`           | `String` | 역할 (아래 참조)  |
| `createdDate`        | `Date`   | 역할 등록일      |

### `userRole` 값

| 값                              | 설명     |
|--------------------------------|--------|
| `STREAMING_CHANNEL_OWNER`      | 채널 소유자 |
| `STREAMING_CHANNEL_MANAGER`    | 채널 매니저 |
| `STREAMING_CHAT_MANAGER`       | 채팅 매니저 |
| `STREAMING_SETTLEMENT_MANAGER` | 정산 매니저 |

---


## 팔로워 목록 조회


내 채널의 팔로워 목록을 페이징으로 조회합니다.

```http
GET /open/v1/channels/followers?page=0&size=30
Authorization: Bearer {accessToken}
```

**인증**: Access Token (사용자 인증 필수)


### 요청 파라미터

| 파라미터   | 타입    | 필수 | 기본값  | 설명               |
|--------|-------|----|------|------------------|
| `page` | `Int` | No | `0`  | 페이지 번호 (0부터 시작)  |
| `size` | `Int` | No | `30` | 페이지당 결과 수 (1~50) |

### 응답 (`data` 배열)

| 필드            | 타입       | 설명          |
|---------------|----------|-------------|
| `channelId`   | `String` | 팔로워의 채널 식별자 |
| `channelName` | `String` | 팔로워의 채널 이름  |
| `createdDate` | `Date`   | 팔로우 날짜      |

---


## 구독자 목록 조회


내 채널의 구독자 목록을 페이징과 정렬 옵션으로 조회합니다.

```http
GET /open/v1/channels/subscribers?page=0&size=30&sort=RECENT
Authorization: Bearer {accessToken}
```

**인증**: Access Token (사용자 인증 필수)


### 요청 파라미터

| 파라미터   | 타입       | 필수 | 기본값  | 설명                                  |
|--------|----------|----|------|-------------------------------------|
| `page` | `Int`    | No | `0`  | 페이지 번호 (0부터 시작)                     |
| `size` | `Int`    | No | `30` | 페이지당 결과 수 (1~50)                    |
| `sort` | `String` | No | -    | `RECENT` (최신순) 또는 `LONGER` (구독 기간순) |

### 응답 (`data` 배열)

| 필드            | 타입       | 설명             |
|---------------|----------|----------------|
| `channelId`   | `String` | 구독자의 채널 식별자    |
| `channelName` | `String` | 구독자의 채널 이름     |
| `month`       | `Int`    | 구독 개월 수        |
| `tierNo`      | `Int`    | 구독 티어 (1 또는 2) |
| `createdDate` | `Date`   | 구독 시작일         |
