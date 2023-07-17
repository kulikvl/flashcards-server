## Start with a base image containing Java runtime
#FROM openjdk:17-jdk-alpine
#
## The application's .jar file
#ARG JAR_FILE=build/libs/flashcards-server-0.0.1-SNAPSHOT.jar
#
## Add the application's .jar to the container
#COPY ${JAR_FILE} app.jar
#
## Run the .jar file
#ENTRYPOINT ["java","-jar","/app.jar"]



FROM gradle:jdk17 AS builder
COPY . /home/gradle/project
WORKDIR /home/gradle/project
RUN ./gradlew clean bootJar

FROM openjdk:17-jdk-alpine
COPY --from=builder /home/gradle/project/build/libs/*.jar /app/server.jar
WORKDIR /app
CMD ["java", "-jar", "server.jar"]



# This Dockerfile is an example of a multi-stage build.
# In the first stage, the project is built with Gradle in a Docker container.
# Then, in the second stage, the output of the build (a JAR file) is copied into a new Docker image which is based on OpenJDK 17.
# The final Docker image does not contain the build tools (Gradle), which makes it smaller.

# run container based on this Dockerfile: docker rmi -f springapi && docker build -t springapi . && docker run -p 8080:8080 springapi

# run interactively: docker run -it springapi /bin/bash (or /bin/sh)

# remove all images and containers: docker stop $(docker ps -a -q)  && docker rm $(docker ps -a -q) && docker rmi $(docker images -a -q)