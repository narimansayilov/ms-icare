FROM openjdk:17-jdk-slim

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} ms-icare.jar

ENTRYPOINT ["java", "-jar", "/ms-icare.jar"]