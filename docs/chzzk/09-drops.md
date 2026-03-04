# 드롭스 (Drops)


> 드롭스 리워드 청구 조회 및 상태 업데이트 API입니다.

**사전 조건**: Drops API Scope 신청이 필요하며, 개발자 센터를 통한 사업자 인증이 필요합니다.

---


## 리워드 청구 조회


드롭스 리워드 청구 목록을 조회합니다.

```http
GET /open/v1/drops/reward-claims
Client-Id: {clientId}
Client-Secret: {clientSecret}
```

**인증**: Client 인증


### 요청 파라미터

| 파라미터               | 타입       | 필수 | 설명                                  |
|--------------------|----------|----|-------------------------------------|
| `page.from`        | `String` | No | 페이지네이션 시작 식별자                       |
| `page.size`        | `Int`    | No | 결과 수 (기본값: 20)                      |
| `claimId`          | `String` | No | 청구 ID (최대 100개, 쉼표 구분)              |
| `channelId`        | `String` | No | 사용자 채널 ID 필터                        |
| `campaignId`       | `String` | No | 캠페인 ID 필터 (`categoryId`와 동시 사용 불가)  |
| `categoryId`       | `String` | No | 카테고리 ID 필터 (`campaignId`와 동시 사용 불가) |
| `fulfillmentState` | `String` | No | `CLAIMED` 또는 `FULFILLED`            |

### 응답

| 필드                 | 타입       | 설명                       |
|--------------------|----------|--------------------------|
| `claimId`          | `String` | 청구 식별자                   |
| `campaignId`       | `String` | 캠페인 식별자                  |
| `rewardId`         | `String` | 리워드 식별자                  |
| `categoryId`       | `String` | 카테고리 식별자                 |
| `categoryName`     | `String` | 카테고리 표시 이름               |
| `channelId`        | `String` | 사용자 채널 식별자               |
| `fulfillmentState` | `String` | `CLAIMED` 또는 `FULFILLED` |
| `claimedDate`      | `String` | 청구 시간 (RFC 3339 UTC)     |
| `updatedDate`      | `String` | 업데이트 시간 (RFC 3339 UTC)   |
| `page.cursor`      | `String` | 다음 페이지 커서 토큰             |

---


## 리워드 상태 업데이트


드롭스 리워드의 이행(fulfillment) 상태를 업데이트합니다.

```http
PUT /open/v1/drops/reward-claims
Client-Id: {clientId}
Client-Secret: {clientSecret}
Content-Type: application/json
```

**인증**: Client 인증


### 요청 본문

| 파라미터               | 타입         | 필수  | 설명                              |
|--------------------|------------|-----|---------------------------------|
| `claimIds`         | `String[]` | Yes | 업데이트할 청구 ID 배열                  |
| `fulfillmentState` | `String`   | Yes | 대상 상태: `CLAIMED` 또는 `FULFILLED` |

### 응답 상태 코드

| 코드              | 설명            |
|-----------------|---------------|
| `SUCCESS`       | 상태 변경 성공      |
| `INVALID_ID`    | 잘못된 요청 ID     |
| `NOT_FOUND`     | 존재하지 않는 청구 ID |
| `UNAUTHORIZED`  | 사용자 연동 해제됨    |
| `UPDATE_FAILED` | 상태 변경 오류      |

---


## `fulfillmentState` 값

| 값           | 설명                 |
|-------------|--------------------|
| `CLAIMED`   | 사용자가 리워드를 청구한 상태   |
| `FULFILLED` | 리워드가 이행(지급) 완료된 상태 |
