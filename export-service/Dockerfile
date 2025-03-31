FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /home/gradle/project
COPY . .
RUN ./gradlew :export-service:bootJar  --no-daemon
#RUN gradle :export-service:bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /home/gradle/project/export-service/build/libs/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
