FROM maven:3.9-amazoncorretto-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

FROM amazoncorretto:17-alpine-jdk
WORKDIR /app

# Install wget for debugging
RUN apk add --no-cache wget

COPY --from=build /app/target/*.jar fx-deals-warehouse.jar

EXPOSE 8080

# Add JVM options for better startup
ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "fx-deals-warehouse.jar"]