FROM openjdk:11-jdk
ARG JAR_FILE=build/libs/freshplace-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djasypt.encryptor.password=${ENC_PASSWORD}", "-jar","/app.jar"]
