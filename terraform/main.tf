terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 3.0"
    }
  }
}

provider "azurerm" {
  features {}
}

# 1. Tạo một cái hộp chứa (Resource Group) tên là rg-springboot-practice
resource "azurerm_resource_group" "rg" {
  name     = "rg-springboot-practice"
  location = "southeastasia"
}

# 2. Tạo gói cấu hình Service Plan (Chọn cấu hình F1 - FREE)
resource "azurerm_service_plan" "plan" {
  name                = "plan-springboot"
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
  os_type             = "Linux"
  sku_name            = "F1" # <--- Gói MIỄN PHÍ của Azure
}

# 3. Tạo một con App Service để kéo Docker image về chạy ứng dụng Web
resource "azurerm_linux_web_app" "app" {
  name                = "app-springboot-backend-lamvu" # Tên này trên Azure không được trùng với ai, nếu trùng bạn đổi đuôi chữ đi nhé
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
  service_plan_id     = azurerm_service_plan.plan.id

  site_config {
    always_on = false # Bắt buộc chọn false đối với gói F1 Free
    application_stack {
      docker_image_name   = "vtlamdev/source-base:latest" # Ban đầu cho chạy tạm nginx, các lần sau nó sẽ tự sync image mới
      docker_registry_url = "https://ghcr.io"
    }
  }

  app_settings = {
    "WEBSITES_PORT" = "8080" # Cổng chạy mặc định bên trong container của Spring Boot
    "DOCKER_REGISTRY_SERVER_URL"      = "https://ghcr.io"
    "DOCKER_REGISTRY_SERVER_USERNAME" = "vtlamdev"
  }
}