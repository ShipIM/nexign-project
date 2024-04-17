FROM openjdk:17-oracle
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} commutator.jar
EXPOSE 8083
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "/commutator.jar"]