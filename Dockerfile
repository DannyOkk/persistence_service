FROM maven:3.9-eclipse-temurin-21-noble AS base
WORKDIR /java/app/base

RUN apt-get update \
 && apt-get install -y build-essential \
 && apt-get purge -y --auto-remove -o APT::AutoRemove::RecommendsImportant=false \
 && rm -rf /var/lib/apt/lists/*

COPY pom.xml .
RUN mvn dependency:go-offline -B

FROM maven:3.9-eclipse-temurin-21-noble AS builder
WORKDIR /java/app/builder

COPY --from=base /root/.m2 /root/.m2
COPY . .
RUN mvn clean package -DskipTests -B

FROM eclipse-temurin:21-jre-alpine AS runtime
ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS

WORKDIR /app

COPY --from=builder /java/app/builder/target/app.jar /app/app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]