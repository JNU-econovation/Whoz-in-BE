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