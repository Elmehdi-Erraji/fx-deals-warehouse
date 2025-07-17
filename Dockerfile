FROM maven:3.9-amazoncorretto-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src src
RUN mvn clean install -DskipTests

FROM eclipse-temurin:17-jre-focal
WORKDIR /app

COPY --from=build /app/target/*.jar fx-deals-warehouse.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/fx-deals-warehouse.jar"]