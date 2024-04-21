FROM openjdk:17-oracle
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} brt-service.jar
EXPOSE 8084
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "/brt-service.jar"]