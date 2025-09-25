# syntax=docker/dockerfile:1
FROM amazoncorretto:17-alpine AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/target/taxshelf-api-1.0.0.jar app.jar
EXPOSE 5000
ENTRYPOINT ["java","-jar","app.jar"]
