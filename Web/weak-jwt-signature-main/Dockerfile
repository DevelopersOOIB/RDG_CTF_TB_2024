FROM gradle:8.7.0-jdk21-alpine AS builder
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

FROM openjdk:21-jdk
COPY --from=builder /app/build/libs/*.jar /app/weak-jwt-signature.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/weak-jwt-signature.jar"]