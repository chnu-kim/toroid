# 활동 제한 (Restriction)


> 채널의 활동 제한을 추가, 삭제, 조회하는 API입니다.

모든 Restriction API는 **Access Token (사용자 인증)** 이 필요합니다.

---


## 활동 제한 추가


특정 채널에 대해 활동 제한을 추가합니다.

```http
POST /open/v1/restrict-channels
Authorization: Bearer {accessToken}
Content-Type: application/json
```

### 요청 본문

| 파라미터              | 타입       | 필수  | 설명        |
|-------------------|----------|-----|-----------|
| `targetChannelId` | `String` | Yes | 제한할 채널 ID |

### 응답


성공 시 HTTP 200을 반환합니다.

---


## 활동 제한 삭제


특정 채널의 활동 제한을 삭제합니다.

```http
DELETE /open/v1/restrict-channels
Authorization: Bearer {accessToken}
Content-Type: application/json
```

### 요청 본문

| 파라미터              | 타입       | 필수  | 설명           |
|-------------------|----------|-----|--------------|
| `targetChannelId` | `String` | Yes | 제한 해제할 채널 ID |

### 응답


성공 시 HTTP 200을 반환합니다.

---


## 활동 제한 목록 조회


현재 채널의 활동 제한 목록을 조회합니다.

```http
GET /open/v1/restrict-channels?size=20&next={next}
Authorization: Bearer {accessToken}
```

### 요청 파라미터

| 파라미터   | 타입       | 필수 | 설명           |
|--------|----------|----|--------------|
| `size` | `Int`    | No | 페이지당 결과 수    |
| `next` | `String` | No | 다음 페이지 커서 토큰 |

### 응답 (`data` 배열)

| 필드                      | 타입       | 설명         |
|-------------------------|----------|------------|
| `restrictedChannelId`   | `String` | 제한된 채널 식별자 |
| `restrictedChannelName` | `String` | 제한된 채널 이름  |
| `createdDate`           | `Date`   | 제한 시작일     |
| `releaseDate`           | `Date`   | 제한 해제일     |
