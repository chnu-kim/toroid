---
paths:
  - src/test/**/*.kt
---

# Test Convention

## 프레임워크

- Kotest `BehaviorSpec`를 표준 spec style로 사용한다.
- JUnit 스타일(`@Test`, `@Nested`, `@BeforeEach`)을 사용하지 않는다.

## 테스트 이름

- `Given` / `When` / `Then` 블록을 한국어로 작성한다.
- 예: `Given("유효하지 않은 code가 주어졌을 때")` → `When("로그인을 시도하면")` → `Then("예외를 발생시킨다")`

## 파일 규칙

- 파일명: `{ClassName}Test.kt`
- 위치: `src/test/kotlin/me/chnu/toroid/{layer}/...` (소스 구조를 미러링)
- 패키지: 소스 클래스와 동일한 패키지

## Mocking

- MockK를 사용한다 (`mockk`, `every`, `verify`, `slot`).
- Spring 컨텍스트 없이 테스트할 수 있으면 `mockk<T>()`를 우선 사용한다.

## Assertions

- Kotest matchers를 사용한다: `shouldBe`, `shouldThrow`, `shouldNotBeNull`, `shouldContain` 등.
- JUnit assertions (`assertEquals`, `assertTrue`)를 사용하지 않는다.

## 레이어별 가이드라인

### Domain Entity (순수 단위 테스트)

- Spring 컨텍스트를 로드하지 않는다.
- 도메인 객체를 mock하지 않는다 — 실제 인스턴스를 사용한다.
- 엔티티의 비즈니스 로직, 값 검증, 상태 전이를 검증한다.
- 도메인 서비스는 순수 단위 테스트 대상이 아니다 — 의존성 mock이 필수이므로 Application 레이어 테스트 전략을 따른다.
- 비즈니스 로직이 없는 엔티티(생성자 + equals/hashCode만 존재)는 테스트를 작성하지 않는다.

### Application / UseCase / Domain Service (단위 테스트)

- 도메인 서비스(`@Service`)도 이 전략을 따른다 — 의존성 mock이 필수이므로 순수 단위 테스트가 아니다.
- 도메인 인터페이스(repository, client, storage)를 `mockk<T>()`로 mock한다.
- UseCase/서비스의 오케스트레이션 로직을 검증한다.
- `every { ... } returns ...`로 의존성 행동을 정의하고, `verify { ... }`로 호출을 검증한다.

### Infrastructure (통합 테스트)

- `@SpringBootTest` 또는 test slice(`@DataJpaTest`)를 사용한다.

### Presentation / Controller (슬라이스 테스트)

- `@WebMvcTest(ControllerClass::class)`를 사용한다.
- UseCase를 mock하여 주입한다.
- `MockMvc`로 HTTP 요청/응답을 검증한다.

## 구조 예시

```kotlin
class ChzzkOAuthUseCaseTest : BehaviorSpec({

    val chzzkAuthService = mockk<ChzzkAuthService>()
    val sut = ChzzkOAuthUseCase(chzzkAuthService)

    Given("유효한 code와 state가 주어졌을 때") {
        every { chzzkAuthService.authenticate(any(), any()) } returns token

        When("사용자를 로드하면") {
            val result = sut.loadUser("valid-code", "valid-state")

            Then("토큰을 반환한다") {
                result.shouldNotBeNull()
            }
        }
    }

    Given("빈 code가 주어졌을 때") {
        When("사용자를 로드하면") {
            Then("InvalidOAuthParameterException을 발생시킨다") {
                shouldThrow<InvalidOAuthParameterException> {
                    sut.loadUser("", "valid-state")
                }
            }
        }
    }
})
```