FROM eclipse-temurin:17-jre-jammy

RUN DEBIAN_FRONTEND=noninteractive apt update && apt install -y --no-install-recommends \
    tshark \
    wireless-tools \
    network-manager \
    arp-scan \
    && rm -rf /var/lib/apt/lists/*

#TODO: nmcli 스크립트, 모니터 모드 설정, iproute2

WORKDIR /app
COPY build/libs/network-api-*.jar app.jar
ENV TZ=Asia/Seoul
ENTRYPOINT ["java", "-jar", "app.jar"]

#ENTRYPOINT ["tail", "-f", "/dev/null"]
