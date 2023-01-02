FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
COPY target/session-service-0.0.1-SNAPSHOT.jar /app
ENV SERVER_PORT 8080
EXPOSE $SERVER_PORT
CMD ["java", "-jar", "session-service-0.0.1-SNAPSHOT.jar" ]