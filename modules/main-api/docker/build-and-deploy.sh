#!/bin/bash

# gradlew로 jar 만들고 도커 컴포즈 실행

set -e
cd "$(dirname "$0")"
cd ../../..
./gradlew :main-api:bootJar
cd -
docker container prune -f
docker image prune -f
docker compose up -d main-api --build