# --------------------
# Stage 1: Build
# --------------------
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml and download dependencies (cache layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the rest of the source code
COPY src ./src

# Package the application (skip tests for faster build, remove if you want tests)
RUN mvn clean package -DskipTests

# --------------------
# Stage 2: Runtime
# --------------------
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy only the built jar from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose port (matches server.port in application.properties, default 8080)
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
