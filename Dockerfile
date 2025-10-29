FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/retail-billing-backend-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
