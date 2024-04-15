FROM openjdk:17-oracle
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} security-service.jar
EXPOSE 8082
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "/security-service.jar"]