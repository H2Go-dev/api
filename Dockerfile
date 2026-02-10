FROM openjdk:25-ea-21-jdk-slim
WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline -B

COPY src ./src
COPY checkstyle.xml .

RUN ./mvnw clean package -DskipTests

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/h2go-0.0.1-SNAPSHOT.jar"]
