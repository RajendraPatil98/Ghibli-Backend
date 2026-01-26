# ============================================================================
# Multi-stage Dockerfile for Ghibli API (Backend)
# Optimized for production deployment on Render.com
# ============================================================================

# Stage 1: Build stage
# Using official Maven image with Java 17
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /build

# Copy pom.xml and download dependencies (layer caching)
COPY pom.xml .
RUN mvn dependency:resolve

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# ============================================================================
# Stage 2: Runtime stage
# Using lightweight JDK runtime image
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Install curl for health checks (minimal size)
RUN apk add --no-cache curl

# Copy the built JAR from builder stage
COPY --from=builder /build/target/ghibliapi-*.jar app.jar

# Expose port (Render will map this)
EXPOSE 8082

# Health check endpoint
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8082/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
