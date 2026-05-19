terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 3.0"
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

# 1. 🚀 ĐỔI TÊN Ở ĐÂY: Tạo một cái rổ MỚI TINH tên là rg-springboot-prod để chứa App
resource "azurerm_resource_group" "rg" {
  name     = "rg-springboot-prod" # <--- Chỉ cần đổi chỗ này sang tên mới tinh thôi!
  location = "southeastasia"
}

# 2. Tạo gói cấu hình Service Plan (Tự động ăn theo rổ mới rg-springboot-prod)
resource "azurerm_service_plan" "plan" {
  name                = "plan-springboot"
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
  os_type             = "Linux"
  sku_name            = "F1"
}

# 3. Tạo App Service (Tự động ăn theo rổ mới rg-springboot-prod)
resource "azurerm_linux_web_app" "app" {
  name                = "app-springboot-backend-lamvu"
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
  service_plan_id     = azurerm_service_plan.plan.id

  site_config {
    always_on = false
    application_stack {
      docker_image_name   = "vtlamdev/source-base:latest"
      docker_registry_url = "https://ghcr.io"
    }
  }

  app_settings = {
    "WEBSITES_PORT"                   = "8080"
    "DOCKER_REGISTRY_SERVER_URL"      = "https://ghcr.io"
    "DOCKER_REGISTRY_SERVER_USERNAME" = "vtlamdev"
  }
}