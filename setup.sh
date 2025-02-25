#!/bin/bash
set -e

# 그래들 빌드를 위한 자바 설치 (17)
if command -v java >/dev/null 2>&1; then
  echo "Java가 이미 설치되어 있습니다."
else
  echo "Java가 설치되어 있지 않습니다. 설치를 진행합니다."

  if command -v apt-get >/dev/null 2>&1; then # Debian 기반
    sudo apt-get update && sudo apt-get install -y openjdk-17-jdk
  else
    echo "지원하지 않는 배포판입니다. 자바는 수동으로 설치하세요."
    exit 1
  fi
fi

# Docker, Docker Compose 설치
if command -v docker &> /dev/null; then
    echo "Docker가 이미 설치되어 있습니다."
else
    echo "Docker가 설치되어 있지 않습니다. 설치를 진행합니다."
    curl -fsSL https://get.docker.com | sudo sh
    echo "Docker 및 Docker compose 설치가 완료되었습니다."
fi
if groups $USER | grep -q '\bdocker\b'; then
    echo "현재 사용자는 이미 Docker 그룹에 속해 있습니다."
else
    echo "현재 사용자를 Docker 그룹에 추가합니다."
    sudo usermod -aG docker $USER
    newgrp docker
fi
