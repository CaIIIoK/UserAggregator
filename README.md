# User Data Aggregation Service

## Overview
This Spring Boot application aggregates user data from multiple databases and provides a single REST endpoint to fetch all users.

## Requirements
1. Java 21
2. Docker
3. At least one known data source related to Postgres, ORACLE, MySQL. 

## Setup

1. Clone the repository.
2. Configure the data sources in `src/main/resources/application.yml`.
3. Build the project:
    ```sh
    ./mvnw clean install
    ```
4. Run the application:
    ```sh
    ./mvnw spring-boot:run
    ```

## Swagger UI
To view the API documentation, go to: [Swagger UI](http://localhost:8080/swagger-ui.html)

## Testing
The project includes integration test using Testcontainers.
Docker should be installed before test run.

### Run Integration Tests:
```sh
./mvnw test