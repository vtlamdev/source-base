terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 3.0"
    }
    random = {
      source  = "hashicorp/random"
      version = "~> 3.6"
    }
  }

  # Giữ nguyên cấu hình backend này để đọc bộ nhớ trong con Storage Account cũ
  backend "azurerm" {
    resource_group_name  = "rg-springboot-practice"
    storage_account_name = "vtlamstoragestate"
    container_name       = "tfstate"
    key                  = "terraform.tfstate"
  }
}

provider "azurerm" {
  features {}
}

resource "random_password" "postgres_admin" {
  length           = 32
  special          = true
  override_special = "!#$%&*()-_=+[]{}:?"
}

resource "random_id" "jwt_signing_key" {
  byte_length = 64
}

resource "azurerm_resource_group" "rg" {
  name     = var.resource_group_name
  location = var.location
}

resource "azurerm_service_plan" "plan" {
  name                = var.service_plan_name
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
  os_type             = "Linux"
  sku_name            = "F1"
}

resource "azurerm_postgresql_flexible_server" "postgres" {
  name                          = var.postgres_server_name
  resource_group_name           = azurerm_resource_group.rg.name
  location                      = azurerm_resource_group.rg.location
  version                       = var.postgres_version
  administrator_login           = var.postgres_admin_username
  administrator_password        = random_password.postgres_admin.result
  storage_mb                    = var.postgres_storage_mb
  sku_name                      = var.postgres_sku_name
  backup_retention_days         = 7
  public_network_access_enabled = true
}

resource "azurerm_postgresql_flexible_server_database" "database" {
  name      = var.postgres_database_name
  server_id = azurerm_postgresql_flexible_server.postgres.id
  charset   = "UTF8"
  collation = "en_US.utf8"
}

resource "azurerm_postgresql_flexible_server_firewall_rule" "allow_azure_services" {
  name             = "allow-azure-services"
  server_id        = azurerm_postgresql_flexible_server.postgres.id
  start_ip_address = "0.0.0.0"
  end_ip_address   = "0.0.0.0"
}

resource "azurerm_redis_cache" "redis" {
  name                          = var.redis_cache_name
  location                      = azurerm_resource_group.rg.location
  resource_group_name           = azurerm_resource_group.rg.name
  capacity                      = var.redis_capacity
  family                        = var.redis_family
  sku_name                      = var.redis_sku_name
  non_ssl_port_enabled          = false
  minimum_tls_version           = "1.2"
  public_network_access_enabled = true
}

resource "azurerm_linux_web_app" "app" {
  name                = var.web_app_name
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
  service_plan_id     = azurerm_service_plan.plan.id

  site_config {
    always_on = false
    application_stack {
      docker_image_name   = var.docker_image_name
      docker_registry_url = var.docker_registry_url
    }
  }

  app_settings = {
    "WEBSITES_PORT"                         = "8080"
    "DOCKER_REGISTRY_SERVER_URL"            = var.docker_registry_url
    "DOCKER_REGISTRY_SERVER_USERNAME"       = var.docker_registry_username
    "SPRING_DATASOURCE_URL"                 = "jdbc:postgresql://${azurerm_postgresql_flexible_server.postgres.fqdn}:5432/${azurerm_postgresql_flexible_server_database.database.name}?sslmode=require"
    "SPRING_DATASOURCE_USERNAME"            = azurerm_postgresql_flexible_server.postgres.administrator_login
    "SPRING_DATASOURCE_PASSWORD"            = random_password.postgres_admin.result
    "SPRING_DATA_REDIS_HOST"                = azurerm_redis_cache.redis.hostname
    "SPRING_DATA_REDIS_PORT"                = tostring(azurerm_redis_cache.redis.ssl_port)
    "SPRING_DATA_REDIS_PASSWORD"            = azurerm_redis_cache.redis.primary_access_key
    "SPRING_DATA_REDIS_SSL_ENABLED"         = "true"
    "SOURCE_BASE_JWT_TOKEN_SIGNING_KEY"     = random_id.jwt_signing_key.b64_std
    "HIBERNATE_SQL_LOG_LEVEL"               = "INFO"
    "HIBERNATE_BIND_LOG_LEVEL"              = "INFO"
  }

  depends_on = [
    azurerm_postgresql_flexible_server_database.database,
    azurerm_postgresql_flexible_server_firewall_rule.allow_azure_services
  ]
}
