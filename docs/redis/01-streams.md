# Redis Streams

> 본 문서는 Redis 8 공식 문서(redis.io/docs)를 기반으로 작성되었다.
> compose.yml 기준 이미지: `redis:8.4.0-alpine`

## 개요

Redis Streams는 append-only 로그 자료구조다. 각 엔트리는 고유한 ID와 하나 이상의 field-value 쌍으로 구성된다.
메시지 브로커, 이벤트 소싱, 실시간 데이터 파이프라인 등에 활용된다.

## Entry ID

- 형식: `<millisecondsTime>-<sequenceNumber>` (예: `1526919030474-0`)
- `XADD`에서 `*`를 지정하면 서버가 자동 생성한다.
- ID는 단조 증가(monotonically increasing)가 보장된다.

---

## 핵심 명령어

### XADD — 엔트리 추가

스트림에 새 엔트리를 추가한다. 스트림이 존재하지 않으면 자동 생성된다.

```redis
XADD mystream * field1 value1 field2 value2
-- "1526919030474-0"
```

**트리밍 옵션** (추가 시 동시에 적용 가능):
- `MAXLEN [=|~] <count>` — 엔트리 수 기준 트리밍
- `MINID [=|~] <id>` — ID 기준 트리밍

`~` 연산자는 정확한 트리밍 대신 근사치로 트리밍하여 성능을 최적화한다.

```redis
XADD mystream MAXLEN ~ 1000 * field1 value1
```

### XLEN — 스트림 길이 조회

```redis
XLEN mystream
-- (integer) 2
```

### XRANGE / XREVRANGE — 범위 조회

```redis
XRANGE mystream - +           -- 전체 조회
XRANGE mystream - + COUNT 10  -- 최대 10개
XREVRANGE mystream + -        -- 역순 조회
```

- `-`: 최소 ID, `+`: 최대 ID

### XREAD — 스트림 읽기

하나 이상의 스트림에서 지정한 ID 이후의 메시지를 읽는다.

```redis
XREAD COUNT 10 STREAMS mystream 0
```

**블로킹 읽기:**

```redis
XREAD BLOCK 5000 STREAMS mystream $
```

- `BLOCK 0`은 무기한 대기
- `$`는 현재 시점 이후의 새 메시지만 수신

### XTRIM — 스트림 트리밍

스트림 크기를 명시적으로 제한한다.

```redis
XTRIM mystream MAXLEN 1000     -- 정확히 1000개로 트리밍
XTRIM mystream MAXLEN ~ 1000   -- 근사 트리밍 (성능 최적화)
XTRIM mystream MINID 1692633198206-0  -- 해당 ID 미만 제거
```

### XDEL — 엔트리 삭제

```redis
XDEL mystream 1526919030474-0
```

---

## Consumer Groups

Consumer Group은 동일 스트림의 메시지를 여러 소비자에게 분배하는 메커니즘이다.
각 메시지는 그룹 내 하나의 소비자에게만 전달된다.

### XGROUP CREATE — 그룹 생성

```redis
XGROUP CREATE mystream mygroup $ MKSTREAM
```

- `$`: 생성 시점 이후 메시지만 소비
- `0`: 스트림의 모든 히스토리 메시지부터 소비
- `MKSTREAM`: 스트림이 없으면 자동 생성

### XREADGROUP — 그룹 읽기

`XREAD`의 consumer group 버전. 메시지를 소비자에게 분배한다.

```redis
XREADGROUP GROUP mygroup consumer1 COUNT 10 STREAMS mystream >
```

- `>`: 아직 전달되지 않은 새 메시지만 요청
- 특정 ID(예: `0`)를 지정하면 해당 소비자의 PEL(Pending Entries List)에서 재조회

**블로킹 읽기:**

```redis
XREADGROUP GROUP mygroup consumer1 BLOCK 5000 COUNT 10 STREAMS mystream >
```

### XACK — 메시지 처리 확인

소비자가 메시지를 성공적으로 처리했음을 그룹에 알린다. ACK된 메시지는 PEL에서 제거된다.

```redis
XACK mystream mygroup 1526919030474-0
```

### XPENDING — 미처리 메시지 조회

PEL(Pending Entries List)의 상태를 조회한다.

```redis
-- 요약 조회
XPENDING mystream mygroup

-- 상세 조회 (범위 + 개수)
XPENDING mystream mygroup - + 10

-- 특정 소비자의 미처리 메시지
XPENDING mystream mygroup - + 10 consumer1

-- 유휴 시간 기준 필터
XPENDING mystream mygroup IDLE 60000 - + 10
```

응답에는 메시지 ID, 소비자 이름, 유휴 시간(idle time), 전달 횟수(delivery count)가 포함된다.

### XCLAIM — 메시지 소유권 이전

장애가 발생한 소비자의 미처리 메시지를 다른 소비자에게 이전한다.

```redis
XCLAIM mystream mygroup consumer2 60000 1526919030474-0
```

- `60000`: 최소 유휴 시간(ms). 이 시간 이상 idle 상태인 메시지만 claim 가능

### XAUTOCLAIM — 자동 소유권 이전

`XPENDING` + `XCLAIM`을 결합한 명령어. 유휴 메시지를 자동으로 식별하고 claim한다.

```redis
XAUTOCLAIM mystream mygroup consumer2 60000 0-0 COUNT 25
```

- 응답: `[다음 커서 ID, claim된 메시지 목록, 삭제된 메시지 ID 목록]`
- 커서가 `0-0`을 반환하면 전체 PEL 스캔 완료

---

## 메시지 전달 보장

| 보장 수준 | 방식 |
|---|---|
| At-most-once | `XREAD`로 읽고 ACK 없이 소비 |
| At-least-once | `XREADGROUP` + `XACK`. 장애 시 PEL을 통해 재전달 |
| Exactly-once | 애플리케이션 레벨에서 멱등성(idempotency) 보장 필요 |

Consumer Group의 기본 동작은 **at-least-once** 전달이다.
`XACK`를 호출하기 전에 소비자가 실패하면, 해당 메시지는 PEL에 남아 `XCLAIM` 또는 `XAUTOCLAIM`으로 다른 소비자에게 재전달될 수 있다.

---

## 참고

- [Redis Streams Introduction](https://redis.io/docs/latest/develop/data-types/streams/)
- [Redis Commands - Streams](https://redis.io/docs/latest/commands/?group=stream)