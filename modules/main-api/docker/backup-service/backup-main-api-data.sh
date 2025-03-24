#!/bin/bash

SOURCE_DIR="/main-api-data"
BACKUP_DIR="/backup/main-api-data"

LOG_FILE="$BACKUP_DIR/backup.log"
TIMESTAMP=$(date +"%Y%m%d-%H%M%S")
FILE_EXTENSION="tar.gz"
BACKUP_FILE="${BACKUP_DIR}/${TIMESTAMP}.${FILE_EXTENSION}"

# 백업 디렉토리 생성 (최초 실행 대비)
mkdir -p "$BACKUP_DIR"

# 백업 수행
tar -czf "$BACKUP_FILE" -C "$SOURCE_DIR" . 2>> "$LOG_FILE"

# 결과 기록
if [ $? -eq 0 ]; then
  FILE_SIZE=$(du -h "$BACKUP_FILE" | cut -f1)
  echo "✅ 백업 완료: $BACKUP_FILE ($(date)) [크기: $FILE_SIZE]" >> "$LOG_FILE"
else
  echo "❌ 백업 실패: $(date)" >> "$LOG_FILE"
fi

/app/cleanup-old-backups.sh "$BACKUP_DIR" "$FILE_EXTENSION" "$LOG_FILE"