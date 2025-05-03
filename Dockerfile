FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copy pom.xml and download dependencies to optimize caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application with explicit packaging
RUN mvn clean package -DskipTests

# Create a lightweight final image
FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
WORKDIR /app

# Copy the built JAR file directly
COPY --from=build /app/target/*.jar /app/app.jar

# Create uploads directory
RUN mkdir -p /app/uploads && chmod 777 /app/uploads

# Set environment variables
ENV UPLOAD_DIR=/app/uploads
ENV SERVER_PORT=4040

EXPOSE 4040

# Simplified startup with the JAR file directly
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--server.port=4040"] 