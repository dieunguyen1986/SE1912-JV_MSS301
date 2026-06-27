# =========================
# Build Stage
# =========================
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /build

COPY . .

RUN mvn clean package -pl services/candidate-service -am -DskipTests

# =========================
# Runtime Stage
# =========================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder  /build/services/candidate-service/target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java","-jar","app.jar"]