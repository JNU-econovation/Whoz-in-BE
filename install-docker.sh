#!/bin/bash

#도커와 도커 컴포즈를 설치함 (리눅스)

set -e

# Docker 설치 여부 확인
if command -v docker &> /dev/null
then
    echo "Docker가 이미 설치되어 있습니다."
else
    echo "Docker가 설치되어 있지 않습니다. 설치를 진행합니다."

    curl -fsSL https://get.docker.com | sudo sh

    echo "Docker 및 Docker compose 설치가 완료되었습니다."
fi

# 현재 사용자를 Docker 그룹에 추가하여 sudo 없이 사용 가능하도록 설정
if groups $USER | grep -q '\bdocker\b'; then
    echo "현재 사용자는 이미 Docker 그룹에 속해 있습니다."
else
    echo "현재 사용자를 Docker 그룹에 추가합니다."
    sudo usermod -aG docker $USER
    newgrp docker
fi
