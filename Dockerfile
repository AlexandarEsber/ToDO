# Build the API
FROM eclipse-temurin:21-jdk-alpine AS api-build
WORKDIR /workspace/api

COPY api/mvnw .
COPY api/.mvn .mvn
COPY api/pom.xml .
COPY api/src src
RUN ./mvnw install -DskipTests

# Package everything together in a small image
FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
COPY --from=api-build /workspace/api/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
