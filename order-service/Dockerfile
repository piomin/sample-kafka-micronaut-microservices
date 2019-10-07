FROM openjdk:12-alpine
ENV JAR_FILE order-service-1.0-SNAPSHOT.jar
ENV APP_HOME /usr/app
EXPOSE 8080
COPY target/$JAR_FILE $JAR_FILE/
WORKDIR $APP_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $JAR_FILE"]