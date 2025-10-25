# ---- Build stage ----
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -q -DskipTests clean package

# ---- Run stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy any jar built from previous stage
COPY --from=build /app/target/*.jar app.jar

# Render provides $PORT automatically
ENV JAVA_OPTS="-Xms256m -Xmx512m"
CMD ["sh","-c","java $JAVA_OPTS -jar app.jar --server.port=${PORT:-5454}"]
