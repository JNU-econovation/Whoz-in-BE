#!/bin/bash

# 운영 서버가 느리기 때문에, 로컬에서 jar 빌드하고 도커 이미지로 빌드해서 도커 허브에 푸시할 수 있도록 만든 스크립트입니다.
# 이 디렉토리에 .env.docker-credentials 파일 만들고 아래에서 요구하는 도커 아이디와 비밀번호 설정해주면 됩니다.

set -e
cd "$(dirname "$0")"
source .env.docker-credentials
echo "main-api 모듈 빌드 중..."
cd ../../..
./gradlew :main-api:bootJar
cd -
echo "Docker Hub 로그인 중..."
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
echo "Docker 이미지 빌드 및 푸시 중..."
docker buildx build \
  --file Dockerfile \
  --tag "$DOCKER_USERNAME"/main-api:latest \
  --push \
  ..