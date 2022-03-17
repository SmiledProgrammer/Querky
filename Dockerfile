FROM openjdk:16
EXPOSE 8080
ARG JAR_FILE=target/querky-0.0.1.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
