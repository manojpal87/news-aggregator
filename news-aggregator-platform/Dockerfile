FROM openjdk:latest

# specific space to persist some data in your container
VOLUME /tmp

# expose application port
EXPOSE 8080

# defines a variable that can be passed to the application at runtime.
ARG JAR_FILE=build/libs/news-aggregator-platform-0.0.1-SNAPSHOT.jar

# add the application to the Docker image from the source path to a destination named app.jar
ADD ${JAR_FILE} app.jar


ENTRYPOINT ["java","-jar","/app.jar"]