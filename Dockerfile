FROM openjdk:11-jdk-slim
VOLUME /tmp
COPY target/my-quintor-declaration-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]