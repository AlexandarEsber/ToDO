# Spring Boot RESTful API

> This is the backend of the Todo API with Spring Boot and MariaDB.
> It exposes REST resources at <http://localhost:8080/api/v1/>.
> See the list of resources in the Swagger UI (<http://localhost:8080/swagger-ui>) or OpenAPI documentation as JSON (<http://localhost:8080/api-docs>).


## Advanced Usage

In case you installed the JDK locally, you can use the Windows Terminal / bash to execute commands:

```bash
# build and package executable --> appears in target/rest-api.jar
./mvnw clean install


# build and package executable without running tests
./mvnw clean install -DskipTests

# run the created JAR file
# --> http://localhost:8080/api/v1/todos
java -jar ./target/rest-api.jar

# for development: build and run in live-reload mode (rebuild on save)
# --> http://localhost:8080/api/v1/todos
./mvnw spring-boot:run
```

After you run the created JAR file, you should be able to see the implemented resources in your browser (<http://localhost:8080/api/v1/todos>).
