# 라이브 (Live)


> 라이브 방송 목록 조회, 스트림 키, 방송 설정 관련 API입니다.

---


## 라이브 목록 조회


현재 진행 중인 라이브 방송 목록을 시청자 수 내림차순으로 조회합니다.

```http
GET /open/v1/lives?size=20&next={next}
Client-Id: {clientId}
Client-Secret: {clientSecret}
```

**인증**: Client 인증


### 요청 파라미터

| 파라미터   | 타입       | 필수 | 기본값  | 설명                            |
|--------|----------|----|------|-------------------------------|
| `size` | `Int`    | No | `20` | 결과 수 (1~20)                   |
| `next` | `String` | No | -    | 이전 응답의 `page.next` 값 (커서 페이징) |

### 응답

| 필드                             | 타입         | 설명                      |
|--------------------------------|------------|-------------------------|
| `data[].liveId`                | `Int`      | 라이브 방송 식별자              |
| `data[].liveTitle`             | `String`   | 방송 제목                   |
| `data[].liveThumbnailImageUrl` | `String`   | 썸네일 이미지 URL             |
| `data[].concurrentUserCount`   | `Int`      | 현재 시청자 수                |
| `data[].openDate`              | `String`   | 방송 시작 시간                |
| `data[].adult`                 | `Boolean`  | 연령 제한 여부                |
| `data[].tags`                  | `String[]` | 방송 태그                   |
| `data[].categoryType`          | `String`   | `GAME`, `SPORTS`, `ETC` |
| `data[].liveCategory`          | `String`   | 카테고리 식별자                |
| `data[].liveCategoryValue`     | `String`   | 카테고리 표시 이름              |
| `data[].channelId`             | `String`   | 스트리머 채널 식별자             |
| `data[].channelName`           | `String`   | 스트리머 채널 이름              |
| `data[].channelImageUrl`       | `String`   | 스트리머 채널 이미지 URL         |
| `page.next`                    | `String`   | 다음 페이지 커서 토큰            |

---


## 스트림 키 조회


현재 사용자의 스트림 키를 조회합니다.

```http
GET /open/v1/streams/key
Authorization: Bearer {accessToken}
```

**인증**: Access Token (사용자 인증 필수)


### 요청 파라미터


없음


### 응답

| 필드          | 타입       | 설명      |
|-------------|----------|---------|
| `streamKey` | `String` | 스트림 키 값 |

---


## 방송 설정 조회


현재 사용자의 방송 설정을 조회합니다.

```http
GET /open/v1/lives/setting
Authorization: Bearer {accessToken}
```

**인증**: Access Token (사용자 인증 필수)


### 요청 파라미터


없음


### 응답

| 필드                        | 타입         | 설명                                |
|---------------------------|------------|-----------------------------------|
| `defaultLiveTitle`        | `String`   | 방송 제목                             |
| `category.categoryType`   | `String`   | 카테고리 타입 (`GAME`, `SPORTS`, `ETC`) |
| `category.categoryId`     | `String`   | 카테고리 식별자                          |
| `category.categoryValue`  | `String`   | 카테고리 표시 이름                        |
| `category.posterImageUrl` | `String`   | 카테고리 이미지 URL                      |
| `tags`                    | `String[]` | 방송 태그 목록                          |

---


## 방송 설정 수정


방송 제목, 카테고리, 태그를 수정합니다.

```http
PATCH /open/v1/lives/setting
Authorization: Bearer {accessToken}
Content-Type: application/json
```

**인증**: Access Token (사용자 인증 필수)


### 요청 본문

| 파라미터               | 타입         | 필수 | 설명                           |
|--------------------|------------|----|------------------------------|
| `defaultLiveTitle` | `String`   | No | 방송 제목 (빈 문자열 불가)             |
| `categoryType`     | `String`   | No | `GAME`, `SPORTS`, `ETC`      |
| `categoryId`       | `String`   | No | 카테고리 ID (빈 문자열로 카테고리 제거)     |
| `tags`             | `String[]` | No | 태그 (빈 배열로 전체 삭제, 공백/특수문자 불가) |

### 응답


방송 설정 조회와 동일한 스키마를 반환합니다.
