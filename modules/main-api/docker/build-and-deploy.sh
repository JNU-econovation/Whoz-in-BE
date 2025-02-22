#!/bin/bash

set -e
cd "$(dirname "$0")"

cd ../../..
./gradlew :main-api:bootJar
cd -
docker container prune -f
docker image prune -f
# main-api 이외엔 재실행되지 않음
# 빌드한 main-api가 변경 사항이 없어도 무조건 재실행 (환경 변수 파일을 변경해도 jar에 포함되지 않기 때문에)
docker compose up -d --build --force-recreate main-api