# =========================
# Build Stage
# =========================
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /build

COPY . .

RUN mvn clean package -pl infra/eureka-server -am -DskipTests

# =========================
# Runtime Stage
# =========================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /build/infra/eureka-server/target/*.jar app.jar

EXPOSE 8761

ENTRYPOINT ["java","-jar","app.jar"]
