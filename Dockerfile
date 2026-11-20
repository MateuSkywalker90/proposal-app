FROM eclipse-temurin:17-jre

WORKDIR /app

# Matches the jar produced by `./mvnw package` regardless of the project version
COPY target/proposal-app-*.jar app.jar

COPY wait-for-it.sh wait-for-it.sh

RUN chmod +x wait-for-it.sh

ENTRYPOINT ["./wait-for-it.sh", "rabbit-mq:5672", "--", "java", "-Duser.language=pt", "-Duser.country=BR", "-jar", "app.jar"]
