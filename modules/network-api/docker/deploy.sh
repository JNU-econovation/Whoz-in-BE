#!/bin/bash
set -e
cd "$(dirname "$0")"

# network-api의 도커 환경은 리눅스에서만 사용할 수 있음.. (맥에선 network_mode: host가 지원하지 않기 때문)
# TODO: 리눅스인지 확인하도록 수정
OS=$(uname -s)
if [ "$OS" = "Darwin" ]; then
    echo "⚠️주의: 맥에서 network-api를 도커로 실행할 경우 tshark가 동작하지 않습니다."
fi

docker container prune -f
docker image prune -f
# 빌드한 main-api가 변경 사항이 없어도 무조건 재실행 (환경 변수 파일을 변경해도 jar에 포함되지 않기 때문에)
docker compose up -d --build --force-recreate network-api