FROM maven:3.8.4-openjdk-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean install
FROM openjdk:17-alpine
COPY --from=builder /app/target/*.jar /app/hard-authorization.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app/hard-authorization.jar"]