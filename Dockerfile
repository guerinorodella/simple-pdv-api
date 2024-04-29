FROM amazoncorretto:21-alpine
COPY build/libs/simple-pdv-api-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]