FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copy pom.xml and download dependencies to optimize caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Create a lightweight final image
FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
ARG DEPENDENCY=/app/target/dependency

# Copy dependencies
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# Create uploads directory
RUN mkdir -p /app/uploads && chmod 777 /app/uploads

# Copy application configuration files
COPY src/main/resources/application.properties /app/application.properties

# Set environment variables
ENV SPRING_CONFIG_LOCATION=file:/app/application.properties
ENV UPLOAD_DIR=/app/uploads
ENV SERVER_PORT=4040

EXPOSE 4040

ENTRYPOINT ["java", "-cp", "app:app/lib/*", "-Dserver.port=4040", "com.psnrwanda.api.ApiApplication"] 