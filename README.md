# Simple Sales Point API

This is a minimalist API for a sales point (PDV) system.

## Running the application

The application, API and the database, are configured via `docker-compose`. Install docker-compose if you haven't yet
and follow instructions bellow:

1. Generate the JAR running the gradle task - `build > Jar` 
2. From a terminal, navigate to repository folder;
3. Run `docker-compose up -d`
4. Access from Browser `http://localhost:8080/*`

## View API doc

After running the application using docker as suggested above, you can access Open API specification from:

http://localhost:8080/api-docs/swagger-ui/index.html

## About it

This is a minimalist implementation of a Sales Point API modeling the product and the orders it may process.

The main goals are:

1. A working Rest API using HTTP verbs to access resources, correct return of error codes etc
2. Fully working environment with Docker - creating the database and the application;
3. Use of Open API with Swagger;
4. Use Semantic commits;

Many of rules and security were not applied on this project, i.e.: user access and rights over endpoints.