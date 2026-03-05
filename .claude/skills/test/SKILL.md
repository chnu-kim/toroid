---
name: test
description: Write Kotest BehaviorSpec tests with MockK for Kotlin classes.
allowed-tools: Read, Edit, Write, Bash(./gradlew test), Bash(./gradlew build)
---

# Test Skill

## Instruction

1. Read the target source file.
2. Read `rules/25-test-convention.md` to confirm conventions and layer strategy.
3. Check existing tests with Glob to avoid duplication.
4. Write or update the test file at mirrored path under `src/test/kotlin/`.
5. Run `./gradlew test` to verify.

## Constraints

- Do NOT write tests for entities with no business logic.
- Do NOT duplicate convention details — `rules/25-test-convention.md` is the source of truth.
