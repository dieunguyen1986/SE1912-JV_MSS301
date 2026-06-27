# =========================
# Build Stage
# =========================
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /build

COPY . .

RUN mvn clean package -pl services/job-service -am -DskipTests

# =========================
# Runtime Stage
# =========================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /build/services/job-service/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","app.jar"]
#FROM maven:3.9-eclipse-temurin-21 AS builder
#WORKDIR /build
#
## build common-web, "install" để Maven nhớ vị trí của nó
#COPY common-web ./common-web
#RUN mvn -f shared/common-web/pom.xml clean install -DskipTests
#
## build service chính - lúc này Maven đã biết common-web ở đâu
#COPY job-service/pom.xml ./services/job-service/pom.xml
#COPY job-service/src ./services/job-service/src
#RUN mvn -f services/job-service/pom.xml clean package -DskipTests
#
## Giai đoạn 2 - runtime: chỉ lấy file .jar của job-service, không lấy gì của common-web
#FROM eclipse-temurin:21-jre-alpine
#WORKDIR /app
#COPY --from=builder /build/job-service/target/*.jar app.jar
#EXPOSE 8081
#ENTRYPOINT ["java", "-jar", "app.jar"]