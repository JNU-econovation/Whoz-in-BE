#!/bin/bash
set -e

# 도커 환경이더라도 호스트에 설정되어야 하는 것들을 설정하는 스크립트

sudo apt update

# network-api
sudo apt install network-manager
