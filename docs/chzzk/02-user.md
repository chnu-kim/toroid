# 사용자 (User)


> 로그인한 사용자의 채널 정보를 조회하는 API입니다.

---


## 내 채널 정보 조회


로그인한 사용자의 채널(channelId, channelName)을 조회합니다.

```http
GET /open/v1/users/me
Authorization: Bearer {accessToken}
```

**인증**: Access Token (사용자 인증 필수)
**스코프**: 사용자 정보 조회


### 요청 파라미터


없음


### 응답

| 필드            | 타입       | 예시                                | 설명         |
|---------------|----------|-----------------------------------|------------|
| `channelId`   | `String` | `909501f048b44cf0d5c1d28XXXXXXXX` | 채널/사용자 식별자 |
| `channelName` | `String` | `치지직유저 3121`                      | 채널 표시 이름   |

```json
{
  "code": 200,
  "message": null,
  "content": {
    "channelId": "909501f048b44cf0d5c1d28XXXXXXXX",
    "channelName": "치지직유저 3121"
  }
}
```

### 참고

- 치지직의 모든 사용자는 채널을 소유합니다.
- `channelId`는 채널 식별자이자 사용자 식별자로 사용됩니다.
