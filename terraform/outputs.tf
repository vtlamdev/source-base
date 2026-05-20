output "web_app_url" {
  value = "https://${azurerm_linux_web_app.app.default_hostname}"
}

output "postgres_fqdn" {
  value = azurerm_postgresql_flexible_server.postgres.fqdn
}

output "redis_hostname" {
  value = azurerm_redis_cache.redis.hostname
}
