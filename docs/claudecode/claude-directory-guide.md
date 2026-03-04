# `.claude` 디렉토리 완전 가이드


Claude Code에서 `.claude` 디렉토리는 **두 가지 스코프**로 존재합니다.

- **글로벌(사용자) 레벨**: `~/.claude/` — 모든 프로젝트에 적용
- **프로젝트 레벨**: `<project>/.claude/` — 해당 프로젝트에만 적용

아래는 각 디렉토리에 존재할 수 있는 파일과 하위 디렉토리, 그리고 그 역할을 정리한 내용입니다.

---


## 1. `settings.json` — 설정 파일


**위치**: `~/.claude/settings.json` (글로벌) / `.claude/settings.json` (프로젝트)

Claude Code의 핵심 설정 파일입니다. 권한(permissions), 훅(hooks), 샌드박싱 등 구조화된 설정을 JSON 형식으로 관리합니다.

```json
{
  "$schema": "https://json.schemastore.org/claude-code-settings.json",
  "permissions": {
    "allow": [ "Bash(npm run lint)", "Read(~/.zshrc)" ],
    "deny": [ "Write(.env)" ]
  },
  "hooks": {
    "PostToolUse": [
      {
        "matcher": "Write(*.py)",
        "hooks": [ { "type": "command", "command": "black \"$file\"" } ]
      }
    ]
  }
}
```

**적용 우선순위** (높은 순):

1. Managed settings (조직 관리자 설정, 오버라이드 불가)
2. 프로젝트 로컬 설정 (`.claude/settings.local.json`)
3. 프로젝트 설정 (`.claude/settings.json`)
4. 사용자 글로벌 설정 (`~/.claude/settings.json`)

---


## 2. `settings.local.json` — 개인 로컬 설정


**위치**: `.claude/settings.local.json` (프로젝트)

`settings.json`과 동일한 형식이지만, `.gitignore`에 자동 추가되어 **버전 관리에 포함되지 않습니다**. 개인 환경에 맞는 실험적 설정이나 개인 선호도를 저장하는 데 사용합니다.

---


## 3. `CLAUDE.md` — 프로젝트 컨텍스트 / 지시사항


**위치**: `~/.claude/CLAUDE.md` (글로벌) / 프로젝트 루트 `CLAUDE.md` / 하위 디렉토리 `CLAUDE.md`

Claude Code가 세션 시작 시 자동으로 읽어들이는 마크다운 파일로, 프로젝트의 "헌법" 역할을 합니다. 코딩 컨벤션, 아키텍처, 빌드/테스트 명령어, 워크플로우 규칙 등을 기술합니다.

**우선순위** (높은 순):

1. 모듈/하위 디렉토리 레벨 (`./src/auth/CLAUDE.md`) — 가장 구체적
2. 프로젝트 루트 레벨 (`./CLAUDE.md`)
3. 사용자 글로벌 레벨 (`~/.claude/CLAUDE.md`) — 가장 일반적

`@` 임포트 구문으로 다른 파일을 참조할 수 있습니다 (예: `@~/.claude/CLAUDE.md`). `/init` 명령어로 자동 생성도 가능합니다.

---


## 4. `commands/` — 커스텀 슬래시 명령어


**위치**: `~/.claude/commands/` (글로벌) / `.claude/commands/` (프로젝트)

마크다운 파일로 정의하는 커스텀 슬래시 명령어입니다. 파일명이 곧 명령어 이름이 됩니다.

```
.claude/commands/
├── test.md          → /test
├── review.md        → /review
├── frontend/
│   └── test.md      → /frontend:test
└── fix-issue.md     → /fix-issue
```

파일 내에서 `$ARGUMENTS`로 인자를 받을 수 있고, YAML 프론트매터로 허용 도구와 설명을 지정할 수 있습니다.

```markdown
---
description: Run project tests
allowed-tools: Bash(npm test:*)
---
Run project tests: !`npm test`
Report results and show details if there are failures.
```

> **참고**: Skills 도입 이후 commands는 레거시로 간주되지만, 여전히 정상 동작합니다.

---


## 5. `skills/` — 스킬 (자동 인식 가능한 지식 패키지)


**위치**: `~/.claude/skills/` (글로벌) / `.claude/skills/` (프로젝트)

스킬은 Claude가 **자연어 맥락에 따라 자동으로 인식하고 적용**할 수 있는 구조화된 역량 패키지입니다. 디렉토리 이름이 스킬 이름이 되며, 내부에 반드시 `SKILL.md` 파일이 있어야 합니다.

```
.claude/skills/
├── pdf-processing/
│   └── SKILL.md
├── testing-patterns/
│   ├── SKILL.md
│   └── templates/
└── brand-voice/
    ├── SKILL.md
    └── guidelines.md
```

SKILL.md 예시:

```markdown
---
name: pdf-processing
description: Extract text, fill forms, merge PDFs. Use when working with PDF files.
allowed-tools: Read, Bash(python:*)
---
# PDF Processing
## Quick start
Extract text:
...
```

**슬래시 명령어와의 차이점**: 명령어는 명시적으로 `/name`으로 호출하지만, 스킬은 Claude가 작업 맥락에 따라 자동으로 선택합니다(확률적). description을 구체적으로 작성할수록 정확하게 트리거됩니다.

---


## 6. `agents/` — 서브에이전트 정의


**위치**: `~/.claude/agents/` (글로벌) / `.claude/agents/` (프로젝트)

독립적인 컨텍스트 윈도우, 커스텀 시스템 프롬프트, 도구 접근 권한을 가진 **전문화된 AI 서브에이전트**를 정의합니다. YAML 프론트매터가 포함된 마크다운 파일로 작성합니다.

```markdown
---
name: code-reviewer
description: Reviews code for quality and best practices
model: sonnet
tools: Read, Grep, Glob
memory: user
---
You are a code review specialist.
Focus on security, performance, and maintainability.
```

주요 프론트매터 필드:

- `name`: 에이전트 이름
- `description`: 자동 호출 트리거 설명
- `model`: 사용할 모델 (sonnet, haiku, opus 등)
- `tools`: 허용할 도구 목록
- `memory`: 메모리 스코프 (user, project, local)

Claude Code에는 `Explore`(읽기 전용 코드 탐색), `Plan`(계획 모드 리서치) 등 빌트인 서브에이전트도 있습니다. `/agents` 명령어로 대화형 생성도 가능합니다.

---


## 7. `rules/` — 경로 기반 모듈형 규칙


**위치**: `.claude/rules/` (프로젝트)

CLAUDE.md를 모듈화하여, **특정 파일 패턴에만 활성화되는 규칙**을 정의합니다. 작업 중인 파일과 관련 없는 규칙은 로드되지 않아 컨텍스트 윈도우를 절약합니다.

```markdown
---
paths:
  - src/api/**/*.ts
---
# API Development Rules
- Use Express router patterns
- Validate all inputs with Zod
- Return standardized error responses
```

```markdown
---
paths:
  - "**/*.test.ts"
---
# Test Writing Standards
- Use descriptive test names
- One assertion per test when possible
- Mock external dependencies
```

CLAUDE.md가 모든 세션에 항상 로드되는 것과 달리, rules는 해당 경로의 파일을 다룰 때만 활성화됩니다.

---


## 8. `hooks/` — 훅 스크립트 저장소


**위치**: `.claude/hooks/` (프로젝트, 관례적)

훅(hooks)은 `settings.json`에 정의되지만, 실행할 쉘 스크립트는 이 디렉토리에 두는 것이 관례입니다.

```
.claude/hooks/
├── skill-eval.sh       # 프롬프트 분석 후 스킬 제안
├── format-check.sh     # 편집 후 자동 포맷팅
└── pre-commit-check.sh # 커밋 전 검증
```

훅 이벤트 종류:

- `SessionStart` / `SessionEnd`: 세션 시작/종료 시
- `UserPromptSubmit`: 사용자 프롬프트 제출 시
- `PreToolUse` / `PostToolUse`: 도구 사용 전/후
- `ConfigChange`: 설정 파일 변경 시

훅은 확률적인 스킬/에이전트와 달리 **100% 결정론적**으로 실행됩니다.

---


## 9. `.credentials.json` — 인증 정보 (Linux/Windows)


**위치**: `~/.claude/.credentials.json`

API 인증 토큰을 저장합니다. macOS에서는 시스템 Keychain을 사용하므로 이 파일이 생성되지 않습니다. 절대 공유하거나 커밋하면 안 됩니다.

---


## 10. `statsig/` — 분석 캐시


**위치**: `~/.claude/statsig/`

Claude Code의 내부 분석 및 피처 플래그 캐시 디렉토리입니다. 사용자가 직접 수정할 필요는 없습니다.

---


## 11. `projects/` — 세션 히스토리


**위치**: `~/.claude/projects/`

프로젝트별 대화 히스토리가 저장됩니다. 프로젝트 경로가 URL 인코딩되어 디렉토리 이름으로 사용됩니다 (예: `/Users/sean/myproject` → `-Users-sean-myproject`).

`claude --resume`이나 `claude --continue`로 이전 세션을 복원할 때 이 데이터를 활용합니다.

---


## 12. 프로젝트 루트의 관련 파일들 (`.claude/` 외부)


`.claude/` 디렉토리 외부지만, Claude Code 설정 체계와 밀접한 파일들도 있습니다.

| 파일               | 위치        | 역할                                                |
|------------------|-----------|---------------------------------------------------|
| `~/.claude.json` | 홈 디렉토리 루트 | OAuth 세션, MCP 서버 설정(user/local), 테마, 알림, 에디터 모드 등 |
| `.mcp.json`      | 프로젝트 루트   | 프로젝트 스코프 MCP(Model Context Protocol) 서버 설정        |
| `CLAUDE.md`      | 프로젝트 루트   | 프로젝트 레벨 지시사항 (팀과 공유, 버전 관리 대상)                    |

---


## 전체 디렉토리 구조 요약

```
~/.claude/                          # 글로벌 (사용자 레벨)
├── settings.json                   # 글로벌 설정
├── settings.local.json             # 글로벌 로컬 설정
├── CLAUDE.md                       # 글로벌 지시사항
├── .credentials.json               # 인증 정보 (Linux/Windows)
├── commands/                       # 글로벌 슬래시 명령어
│   └── *.md
├── skills/                         # 글로벌 스킬
│   └── <skill-name>/SKILL.md
├── agents/                         # 글로벌 서브에이전트
│   └── *.md
├── statsig/                        # 분석 캐시
└── projects/                       # 세션 히스토리

<project>/
├── CLAUDE.md                       # 프로젝트 지시사항 (git 추적)
├── .mcp.json                       # MCP 서버 설정
└── .claude/                        # 프로젝트 레벨
    ├── settings.json               # 프로젝트 설정 (git 추적, 팀 공유)
    ├── settings.local.json         # 프로젝트 로컬 설정 (git 제외)
    ├── commands/                   # 프로젝트 슬래시 명령어
    │   └── *.md
    ├── skills/                     # 프로젝트 스킬
    │   └── <skill-name>/SKILL.md
    ├── agents/                     # 프로젝트 서브에이전트
    │   └── *.md
    ├── rules/                      # 경로 기반 규칙
    │   └── *.md
    └── hooks/                      # 훅 스크립트 (관례)
        └── *.sh
```

---


## 구성요소 비교표

| 구성요소            | 실행 방식           | 특성           | 주요 용도          |
|-----------------|-----------------|--------------|----------------|
| `CLAUDE.md`     | 자동 로드           | 결정론적 (항상 읽힘) | 프로젝트 컨텍스트, 컨벤션 |
| `settings.json` | 자동 적용           | 결정론적         | 권한, 훅, 샌드박싱 설정 |
| `commands/`     | `/명령어`로 명시 호출   | 결정론적         | 반복 프롬프트 자동화    |
| `skills/`       | 자연어 맥락 자동 인식    | 확률적          | 도메인별 지식 패키지    |
| `agents/`       | 작업 매칭 자동 위임     | 확률적          | 전문화된 독립 작업     |
| `rules/`        | 파일 경로 매칭 자동 활성화 | 결정론적         | 경로별 코딩 규칙      |
| Hooks           | 이벤트 트리거 자동 실행   | 결정론적 (100%)  | 포매팅, 검증, 알림    |
