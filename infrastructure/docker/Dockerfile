# One Dockerfile for every service. Build from the repo root:
#   docker build -f infrastructure/docker/Dockerfile --build-arg SERVICE=user-service .
FROM maven:3.9-eclipse-temurin-21 AS build
ARG SERVICE
WORKDIR /src
COPY pom.xml .
COPY services services
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -ntp -pl services/${SERVICE} -am -DskipTests package \
 && cp services/${SERVICE}/target/${SERVICE}-*.jar /app.jar

FROM eclipse-temurin:21-jre
RUN useradd --system --uid 10001 app
USER app
WORKDIR /app
COPY --from=build /app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]
