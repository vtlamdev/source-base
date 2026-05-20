variable "location" {
  type    = string
  default = "southeastasia"
}

variable "resource_group_name" {
  type    = string
  default = "rg-springboot-prod"
}

variable "service_plan_name" {
  type    = string
  default = "plan-springboot"
}

variable "web_app_name" {
  type    = string
  default = "app-springboot-backend-lamvu"
}

variable "docker_image_name" {
  type    = string
  default = "vtlamdev/source-base:latest"
}

variable "docker_registry_url" {
  type    = string
  default = "https://ghcr.io"
}

variable "docker_registry_username" {
  type    = string
  default = "vtlamdev"
}

variable "postgres_server_name" {
  type    = string
  default = "pg-source-base-lamvu"
}

variable "postgres_database_name" {
  type    = string
  default = "source_base"
}

variable "postgres_admin_username" {
  type    = string
  default = "sourcebaseadmin"
}

variable "postgres_version" {
  type    = string
  default = "16"
}

variable "postgres_sku_name" {
  type    = string
  default = "B_Standard_B1ms"
}

variable "postgres_storage_mb" {
  type    = number
  default = 32768
}

variable "redis_cache_name" {
  type    = string
  default = "redis-source-base-lamvu"
}

variable "redis_capacity" {
  type    = number
  default = 0
}

variable "redis_family" {
  type    = string
  default = "C"
}

variable "redis_sku_name" {
  type    = string
  default = "Basic"
}
