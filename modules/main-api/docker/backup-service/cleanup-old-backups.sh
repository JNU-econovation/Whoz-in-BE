#!/bin/bash

# 100일 넘은 백업 파일들을 삭제함 (최소 1개는 남겨둠)

TARGET_DIR="$1"
EXTENSION="$2"
LOG_FILE="$3"

log() {
    echo "$1" >> "$LOG_FILE"
}

# 100일 넘은 백업 파일 (오래된 순 정렬)
mapfile -t DELETE_CANDIDATES < <(find "$TARGET_DIR" -type f -mtime +100 -name "*.${EXTENSION}" | sort)
NUM_CANDIDATES=${#DELETE_CANDIDATES[@]}

# 항상 1개는 남기고 삭제
for (( i=0; i<NUM_CANDIDATES-1; i++ )); do
  FILE="${DELETE_CANDIDATES[$i]}"
  log "오래된 파일 삭제: $FILE"
  rm -f "$FILE"
done
