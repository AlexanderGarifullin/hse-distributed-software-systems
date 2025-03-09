FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /home/gradle/project

COPY . .

RUN ./gradlew :web-service:bootJar --no-daemon
#RUN gradle :web-backend:bootJar --no-daemon


FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /home/gradle/project/web-service/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
