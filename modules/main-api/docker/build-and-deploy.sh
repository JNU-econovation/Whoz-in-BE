#!/bin/bash

set -e
cd "$(dirname "$0")"

# 변수 파일 불러오기
set -a
source .env
set +a
# 도커 컴포즈가 요구하는 init.sql 생성
envsubst < ./mysql/init.sql.template > ./mysql/init.sql

cd ../../..
./gradlew :main-api:bootJar
cd -
docker container prune -f
docker image prune -f
# main-api 이외엔 재실행되지 않음
# main-api 변경 사항이 없으면 재실행되지 않음
docker compose up -d main-api --build