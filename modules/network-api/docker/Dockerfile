FROM eclipse-temurin:17-jre-jammy

RUN DEBIAN_FRONTEND=noninteractive apt update && apt install -y --no-install-recommends \
    tshark \
    wireless-tools \
    network-manager \
    arp-scan \
    iw \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY build/libs/network-api-*.jar app.jar
ENV TZ=Asia/Seoul
ENTRYPOINT ["java", "-jar", "app.jar"]

#ENTRYPOINT ["tail", "-f", "/dev/null"]
