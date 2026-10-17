FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B package

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
RUN addgroup -S fitmanager && adduser -S fitmanager -G fitmanager
COPY --from=build /app/target/fitmanager-0.0.1-SNAPSHOT.jar app.jar

ENV SERVER_PORT=8080
EXPOSE 8080
USER fitmanager

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
