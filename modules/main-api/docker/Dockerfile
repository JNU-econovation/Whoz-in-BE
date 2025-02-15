FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY build/libs/main-api-*.jar app.jar
ENV TZ=Asia/Seoul
ENTRYPOINT ["java", "-jar", "app.jar"]