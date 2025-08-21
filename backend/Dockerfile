FROM eclipse-temurin:24-jdk-alpine AS dev

WORKDIR /app

COPY mvnw mvnw.cmd ./
COPY .mvn .mvn

RUN chmod +x mvnw

COPY pom.xml ./
# Better dependency caching for Maven
RUN ./mvnw dependency:go-offline 

EXPOSE 8080

CMD ["./mvnw", "spring-boot:run"]