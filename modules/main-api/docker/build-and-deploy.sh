#!/bin/bash

set -e
cd "$(dirname "$0")"

cd ../../..
./gradlew :main-api:bootJar
cd -

./deploy.sh