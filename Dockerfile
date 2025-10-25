# Use a valid Maven + JDK 17 base image
FROM maven:3.8.7-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Use a lightweight OpenJDK 17 image for running the app
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy built jar from previous stage
COPY --from=build /app/target/projectify-0.0.1-SNAPSHOT.jar app.jar

# Expose the port used by the app
EXPOSE 5454

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=${PORT:-5454}"]
