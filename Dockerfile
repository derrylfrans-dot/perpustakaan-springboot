# Tahap 1: build file .jar dengan Maven + JDK 17
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -q package -DskipTests

# Tahap 2: jalankan .jar memakai JRE saja (image lebih kecil)
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# H2 console hanya untuk lokal; dimatikan di server publik
ENV SPRING_H2_CONSOLE_ENABLED=false

EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]
