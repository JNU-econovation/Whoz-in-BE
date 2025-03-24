#!/bin/bash

BACKUP_DIR="/backup/command-db"

TIMESTAMP=$(date +"%Y%m%d-%H%M%S")
BACKUP_SQL="$BACKUP_DIR/${TIMESTAMP}.sql"
FILE_EXTENSION="gz"
BACKUP_FILE="${BACKUP_SQL}.${FILE_EXTENSION}"
LOG_FILE="$BACKUP_DIR/backup.log"

mkdir -p "$BACKUP_DIR"

# 백업
mysqldump -h command-db -uroot --password="$MYSQL_ROOT_PASSWORD" --databases whozin > "$BACKUP_SQL"

if [ $? -eq 0 ]; then
  # 압축
  gzip "$BACKUP_SQL"
  # 크기 계산
  FILE_SIZE=$(du -h "$BACKUP_FILE" | cut -f1)
  echo "✅ 백업 완료: $BACKUP_FILE ($(date)) [크기: $FILE_SIZE]" >> "$LOG_FILE"
else
  echo "❌ 백업 실패: $(date)" >> "$LOG_FILE"
fi

/app/cleanup-old-backups.sh "$BACKUP_DIR" "$FILE_EXTENSION" "$LOG_FILE"