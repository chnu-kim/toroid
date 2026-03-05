#!/usr/bin/env bash
set -euo pipefail

INPUT="$(cat)"
TOOL_NAME="$(echo "$INPUT" | jq -r '.tool_name')"

is_secret_path() {
  local p="$1"
  case "$p" in
    *.pem|*.key) return 0 ;;
    *.env|*/.env|*.env.*|*/.env.*) return 0 ;;
    */secrets/*|secrets/*) return 0 ;;
    */resources/keys/*) return 0 ;;
    *) return 1 ;;
  esac
}

case "$TOOL_NAME" in
  Read|Edit|Write)
    FP="$(echo "$INPUT" | jq -r '.tool_input.file_path // empty')"
    if [ -n "$FP" ] && is_secret_path "$FP"; then
      echo "Blocked: secret file access '$FP'" >&2
      exit 2
    fi
    ;;
  Bash)
    CMD="$(echo "$INPUT" | jq -r '.tool_input.command // empty')"
    if echo "$CMD" | grep -Eqi '\.(env|pem|key)\b|secrets/|resources/keys/'; then
      echo "Blocked: bash command references secret files" >&2
      exit 2
    fi
    ;;
esac

exit 0