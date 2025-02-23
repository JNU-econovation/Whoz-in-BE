#!/bin/bash
set -e
cd "$(dirname "$0")"

cd ../../..
./gradlew :network-api:bootJar
cd -

./deploy.sh