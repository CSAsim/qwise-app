# Stage 1: Build the app
FROM gradle:8.5.0-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# Stage 2: Run the app
FROM openjdk:21-jdk
WORKDIR /app
RUN mkdir -p /app/logs && chmod -R 777 /app/logs
COPY --from=builder /app/build/libs/qwise-demo-app-1.0.0.jar app.jar
EXPOSE 7775
ENTRYPOINT ["java", "-jar", "app.jar"]