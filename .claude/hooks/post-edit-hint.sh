#!/usr/bin/env bash
set -euo pipefail

INPUT="$(cat)"
FP="$(echo "$INPUT" | jq -r '.tool_input.file_path // empty')"

# Kotlin 파일이 아니면 스킵
if [[ ! "$FP" =~ \.kts?$ ]]; then
  exit 0
fi

echo "Kotlin file modified. Run './gradlew detekt' to check style."

# src/main/kotlin 파일이면 테스트 안내도 추가
if [[ "$FP" == src/main/kotlin/* ]]; then
  echo "Run './gradlew test' to verify changes."
fi

exit 0