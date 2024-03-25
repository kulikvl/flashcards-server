FROM gradle:jdk17 AS builder
COPY . /home/gradle/project
WORKDIR /home/gradle/project
RUN ./gradlew clean bootJar

FROM openjdk:17-jdk-alpine
COPY --from=builder /home/gradle/project/build/libs/*.jar /app/server.jar
WORKDIR /app
CMD ["java", "-jar", "server.jar"]
