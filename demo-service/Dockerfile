FROM openjdk:17-oracle
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} demo-service.jar
EXPOSE 8081
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "/demo-service.jar"]