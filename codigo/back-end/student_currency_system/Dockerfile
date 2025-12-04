# Build do backend
FROM maven:3.9.9-eclipse-temurin-17 AS build-backend
WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline

COPY . ./

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk
WORKDIR /app

COPY --from=build-backend /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]