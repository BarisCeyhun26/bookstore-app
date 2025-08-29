# Build stage
FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml
COPY pom.xml .

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre

WORKDIR /work/
RUN chown 1001 /work \
    && chmod "g+rwX" /work \
    && chown 1001:root /work

COPY --chown=1001:root --from=build /app/target/quarkus-app/lib/ /work/application/lib/
COPY --chown=1001:root --from=build /app/target/quarkus-app/quarkus-run.jar /work/application/
COPY --chown=1001:root --from=build /app/target/quarkus-app/app/ /work/application/app/
COPY --chown=1001:root --from=build /app/target/quarkus-app/quarkus/ /work/application/quarkus/

EXPOSE 8080
USER 1001

ENV JAVA_OPTIONS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/work/application/quarkus-run.jar"

CMD ["java", "-jar", "/work/application/quarkus-run.jar"]
