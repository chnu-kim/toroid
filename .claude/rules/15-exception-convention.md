---
paths:
  - src/**/*.kt
---

# Exception & Validation Convention

## 입력 검증 위치
- presentation 레이어(Controller)에서 비즈니스 검증을 수행하지 않는다.
- 입력 검증은 application(UseCase) 또는 domain 레이어에서 수행한다.

## 예외 규칙
- 클라이언트에 전달되는 예외에 `IllegalArgumentException`, `IllegalStateException`을 사용하지 않는다.
- `ServerException`을 상속하는 custom exception을 정의하여 사용한다.
- custom exception에는 적절한 HTTP 상태 코드(`statusCode`)를 지정한다.