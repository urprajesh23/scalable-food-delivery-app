# --- STAGE 1: BUILD THE APP ---
# Use a Maven image with Java 17 to build the project
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory inside the server
WORKDIR /app

# Copy the project files into the server
COPY . .

# Build the app (skip tests to speed it up)
RUN mvn clean package -DskipTests

# --- STAGE 2: RUN THE APP ---
# Use a lightweight Java 17 runtime
FROM eclipse-temurin:17-jdk-alpine

# Copy the built JAR file from Stage 1
COPY --from=build /app/target/*.jar app.jar

# Tell Render which port we are using
EXPOSE 8080

# The command to start the app
ENTRYPOINT ["java","-jar","/app.jar"]