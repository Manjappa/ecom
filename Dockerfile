# Build context must be the repository root (parent POM + modules).
# Default image runs order-service. For product-service:
#   docker build --build-arg SERVICE=product-service -t product-service .
ARG SERVICE=order-service

FROM eclipse-temurin:21-jdk-jammy AS builder
ARG SERVICE
WORKDIR /src
COPY . .
RUN chmod +x mvnw
RUN ./mvnw -pl "${SERVICE}" -am package -DskipTests -B

FROM eclipse-temurin:21-jre-jammy
ARG SERVICE
WORKDIR /app
COPY --from=builder "/src/${SERVICE}/target/"*.jar /app/app.jar
EXPOSE 8080
# Cloud Run and Cloud Functions (2nd gen) set PORT at runtime.
ENTRYPOINT ["sh", "-c", "exec java -Dserver.port=${PORT:-8080} -jar /app/app.jar"]
