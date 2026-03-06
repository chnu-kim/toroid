---
paths:
  - src/**/infrastructure/**/*Redis*.kt
  - src/**/infrastructure/**/*Stream*.kt
  - src/**/config/redis/**/*.kt
---

# Redis Streams 구현 규칙

## 사전 참조 필수

작업 전 아래 문서를 반드시 Read 도구로 읽을 것:
- @docs/redis/01-streams.md

## 명령어 사용 규칙

- Stream 추가 시 `XADD`를 사용하고, ID는 `*`(자동 생성)를 기본으로 한다.
- Consumer Group 생성 시 `XGROUP CREATE ... MKSTREAM`으로 스트림 미존재 시 자동 생성한다.
- Consumer Group 읽기는 `XREADGROUP`을 사용하고, 새 메시지에는 `>` ID를 사용한다.
- 메시지 처리 완료 후 반드시 `XACK`를 호출하여 PEL에서 제거한다.
- 장애 복구 시 `XAUTOCLAIM`을 우선 사용한다 (`XPENDING` + `XCLAIM` 수동 조합보다 간결).

## 스트림 관리

- 무한 증가를 방지하기 위해 `XADD` 시 `MAXLEN ~`로 근사 트리밍을 적용한다.
- 정확한 트리밍이 필요한 경우에만 `MAXLEN =`을 사용한다.

## Key 네이밍

- 스트림 키: `{domain}:{event}` 형식 (예: `chat:message`, `live:status`)
- Consumer Group 이름: `{service}-group` 형식 (예: `relay-group`)
- Consumer 이름: `{service}-{instance}` 형식 (예: `relay-1`)

## 메시지 전달 보장

- 기본 전달 보장 수준은 at-least-once로 한다 (`XREADGROUP` + `XACK`).
- 멱등성이 필요한 처리는 메시지 ID를 기반으로 애플리케이션 레벨에서 중복 체크한다.