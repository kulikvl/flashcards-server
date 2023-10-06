FROM gradle:jdk17 AS builder
COPY . /home/gradle/project
WORKDIR /home/gradle/project
RUN ./gradlew clean bootJar

FROM openjdk:17-jdk-alpine
COPY --from=builder /home/gradle/project/build/libs/*.jar /app/server.jar
WORKDIR /app
CMD ["java", "-jar", "server.jar"]


## Description of this Dockerfile:
# This Dockerfile defines a multi-stage build.
# In the first stage (... AS builder), the project is built with Gradle in a Docker container.
# Then, in the second stage, the output of the build (a JAR file from the first stage) is copied into a new Docker image which is based on OpenJDK 17.
# The final Docker image does not contain the build tools (Gradle), which makes it smaller (ensure that the final Docker image doesn't contain all the build-time dependencies, just the application and its runtime dependencies).

## Commands for debugging purposes:
# run container based on this Dockerfile: docker rmi -f flashcards-server && docker build -t flashcards-server . && docker run -p 8080:8080 flashcards-server
# or run interactively: docker run -it springapi /bin/bash
# remove all images and containers: docker stop $(docker ps -a -q)  && docker rm $(docker ps -a -q) && docker rmi $(docker images -a -q)