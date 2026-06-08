FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -q -DskipTests clean package

FROM eclipse-temurin:21-jre
WORKDIR /app

# Run as non-root for GKE/container security best practices.
RUN useradd -r -u 1001 appuser
COPY --from=build /app/target/*.jar app.jar
RUN chown appuser:appuser /app/app.jar

USER 1001
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]

