#!/bin/bash

set -e
cd "$(dirname "$0")"

docker container prune -f
docker image prune -f

docker compose build main-api backup-service
docker compose up -d --force-recreate main-api # 무조건 재시작 (환경변수 적용때문)
docker compose up -d backup-service # 변경된 경우에만 재시작