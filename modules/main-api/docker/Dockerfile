FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY build/libs/main-api-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]