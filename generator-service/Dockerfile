FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /home/gradle/project
COPY . .
RUN ./gradlew :generator-service:bootJar  --no-daemon
#RUN gradle ::bootJar --no-daemon


FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /home/gradle/project/generator-service/build/libs/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
