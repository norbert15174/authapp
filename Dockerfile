FROM maven:3.8.5-openjdk-17-slim AS builder

WORKDIR /build
COPY . .
RUN mvn clean package -Dmaven.compiler.source=17 -Dmaven.compiler.target=17 -DskipTests

FROM openjdk:17-ea-slim
COPY --from=builder /build/target/authapp-0.0.1-SNAPSHOT.jar  /apps/app.jar
USER appuser
ENTRYPOINT ["bash", "-c", "java -Xms256m -Xmx512m -jar /app/app.jar"]
EXPOSE 8080
