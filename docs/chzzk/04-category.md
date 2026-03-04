# 카테고리 (Category)


> 카테고리 검색 API입니다.

---


## 카테고리 검색


카테고리 이름으로 검색합니다.

```http
GET /open/v1/categories/search?query={query}&size=20
Client-Id: {clientId}
Client-Secret: {clientSecret}
```

**인증**: Client 인증


### 요청 파라미터

| 파라미터    | 타입       | 필수  | 기본값  | 설명          |
|---------|----------|-----|------|-------------|
| `query` | `String` | Yes | -    | 카테고리 이름 검색어 |
| `size`  | `Int`    | No  | `20` | 결과 수 (1~50) |

### 응답 (`data` 배열)

| 필드               | 타입       | 설명               |
|------------------|----------|------------------|
| `categoryType`   | `String` | 카테고리 타입          |
| `categoryId`     | `String` | 카테고리 식별자         |
| `categoryValue`  | `String` | 카테고리 표시 이름       |
| `posterImageUrl` | `String` | 카테고리 포스터 이미지 URL |

### `categoryType` 값

| 값        | 설명                                           |
|----------|----------------------------------------------|
| `GAME`   | 게임                                           |
| `SPORTS` | 스포츠                                          |
| `ETC`    | 기타 (토크/캠방, ASMR, 음악/노래, 미술, 먹방/요리, 뷰티, 여행 등) |

```json
{
  "code": 200,
  "message": null,
  "content": {
    "data": [
      {
        "categoryType": "GAME",
        "categoryId": "League_of_Legends",
        "categoryValue": "리그 오브 레전드",
        "posterImageUrl": "https://..."
      }
    ]
  }
}
```
