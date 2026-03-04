# 에러 처리 (Error Handling)


> Chzzk API의 HTTP 상태 코드, 에러 메시지, 응답 구조를 정리한 문서입니다.

---


## HTTP 상태 코드

| HTTP 상태 | 에러 코드                   | 설명                |
|---------|-------------------------|-------------------|
| `400`   | (입력값 오류 메시지)            | 잘못된 요청 파라미터       |
| `401`   | `UNAUTHORIZED`          | 인증 정보 누락          |
| `401`   | `INVALID_CLIENT`        | 잘못된 Client 인증 정보  |
| `401`   | `INVALID_TOKEN`         | 만료되었거나 존재하지 않는 토큰 |
| `403`   | `FORBIDDEN`             | 권한 부족             |
| `404`   | `NOT_FOUND`             | 요청 대상을 찾을 수 없음    |
| `429`   | `TOO_MANY_REQUESTS`     | Rate Limit 초과     |
| `500`   | `INTERNAL_SERVER_ERROR` | 서버 내부 오류          |

---


## 응답 구조 비교


### 성공 응답

```json
{
  "code": 200,
  "message": null,
  "content": {
    "channelId": "abc123...",
    "channelName": "치지직유저"
  }
}
```

### 실패 응답

```json
{
  "code": 401,
  "message": "INVALID_TOKEN"
}
```

| 필드        | 성공 시   | 실패 시         |
|-----------|--------|--------------|
| `code`    | `200`  | HTTP 에러 코드   |
| `message` | `null` | 에러 메시지 문자열   |
| `content` | 응답 데이터 | `null` 또는 생략 |

---


## 에러 처리 권장사항

- `401 INVALID_TOKEN` 수신 시 → Refresh Token으로 토큰 갱신 후 재시도
- `429 TOO_MANY_REQUESTS` 수신 시 → Exponential backoff 적용 ([12-rate-limit.md](./12-rate-limit.md) 참조)
- `500 INTERNAL_SERVER_ERROR` 수신 시 → 재시도 또는 서비스 상태 확인
- 모든 에러 응답에 대해 안전한 에러 메시지만 클라이언트에 반환 (내부 상세 노출 금지)

---


## 코드베이스 매핑


프로젝트에서 Chzzk API 에러는 `ChzzkApiException`으로 변환됩니다.

```kotlin
// domain/chzzk/ChzzkApiException.kt
class ChzzkApiException(message: String, code: Int)

// infrastructure/chzzk/ChzzkClientImpl.kt
private fun <T> ChzzkResponse<T>?.validateChzzkResponse(): T {
    val response = this
        ?: throw ChzzkApiException("Chzzk API response content is null", 500)
    response.message?.let {
        throw ChzzkApiException(it, response.code)
    }
    return response.content
        ?: throw ChzzkApiException("Chzzk API response content is null with status ${response.code}", 500)
}
```
