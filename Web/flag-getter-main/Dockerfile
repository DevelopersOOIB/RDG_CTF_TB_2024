FROM maven:3.8.4-openjdk-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean install
FROM tomcat:9.0.59-jre11-temurin-focal
COPY --from=builder /app/target/flag-getter-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/
EXPOSE 8080