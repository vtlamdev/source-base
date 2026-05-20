# source-base

## Runtime Configuration

Production DB, Redis, and JWT signing key are not hard-coded in
`application.properties`. They are injected as environment variables when the
container starts.

Terraform manages the Azure PostgreSQL Flexible Server and Azure Cache for
Redis resources, then passes their connection values into the Azure Linux Web
App through `app_settings`. In Azure App Service, those settings become
environment variables available to the Docker container at runtime.

For a local Docker run, pass the same variables explicitly:

```bash
docker run --rm -p 8080:8080 \
  -e SPRING_DATASOURCE_URL='jdbc:postgresql://<postgres-host>:5432/source_base?sslmode=require' \
  -e SPRING_DATASOURCE_USERNAME='<postgres-user>' \
  -e SPRING_DATASOURCE_PASSWORD='<postgres-password>' \
  -e SPRING_DATA_REDIS_HOST='<redis-host>' \
  -e SPRING_DATA_REDIS_PORT='6380' \
  -e SPRING_DATA_REDIS_PASSWORD='<redis-password>' \
  -e SPRING_DATA_REDIS_SSL_ENABLED='true' \
  -e SOURCE_BASE_JWT_TOKEN_SIGNING_KEY='<base64-encoded-secret>' \
  ghcr.io/vtlamdev/source-base:latest
```

The GitHub Actions deploy workflow runs Terraform after pushing the Docker
image, so infrastructure and runtime configuration are applied together.
