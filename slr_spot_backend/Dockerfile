FROM openjdk:17-oracle
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src ./src
EXPOSE 8080
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.profiles=docker"]