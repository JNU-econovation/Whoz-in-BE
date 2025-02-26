#!/bin/bash
set -e

# 도커 환경이더라도 호스트에 설정되어야 하는 것들을 설정하는 스크립트

# 필요한 패키지 목록
PACKAGES=(
    network-manager
    # 쉼표 없이 나열
)

# apt-get이 있는지 확인
if ! command -v apt-get >/dev/null 2>&1; then
    echo "지원하지 않는 패키지 매니저입니다. 스크립트를 보고 패키지를 직접 설치하세요."
    exit 1
fi

# 패키지 확인 및 설치
for package in "${PACKAGES[@]}"; do
    if dpkg -l "$package" >/dev/null 2>&1; then
        echo "$package 이미 설치됨"
    else
        echo "$package 설치 중..."
        sudo apt-get install -y --no-install-recommends "$package"
    fi
done
