FROM registry.access.redhat.com/ubi8/openjdk-17:latest

WORKDIR /app

COPY target/sistema-gestao-crises-1.0.0-runner /app/application

RUN chmod +x /app/application

EXPOSE 8080

CMD ["/app/application", "-Dquarkus.http.host=0.0.0.0"]
