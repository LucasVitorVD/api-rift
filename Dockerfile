FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .

RUN apt-get install maven -y
RUN mvn clean install

FROM openjdk:17-jdk-slim

EXPOSE 8080

COPY --from=build /target/api-rift-0.0.1-SNAPSHOT.jar app.jar

COPY /src/main/resources/db/migration /app/db/migration

ENTRYPOINT [ "java", "-jar", "app.jar" ]