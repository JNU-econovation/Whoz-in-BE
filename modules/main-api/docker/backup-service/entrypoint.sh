#!/bin/bash

# 환경변수 반영된 crontab 생성
envsubst < /app/crontab.template.txt > /app/crontab.txt
rm /app/crontab.template.txt
crontab /app/crontab.txt
cron -f