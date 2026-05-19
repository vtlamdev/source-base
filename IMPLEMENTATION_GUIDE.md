# Source Base Implementation Guide

## Purpose
`source-base` là single-module base source được dựng theo hướng ThingsBoard, nhưng không tách thành nhiều Maven module như source gốc. Mục tiêu là giữ một nền backend thống nhất để sau này thêm entity nghiệp vụ, service nghiệp vụ, queue integration, transport hoặc feature riêng mà không phải dựng lại khung hạ tầng.

## Design Direction
- Tham chiếu trực tiếp từ `thingsboard/common/data`, `thingsboard/dao`, `thingsboard/application`.
- Chỉ lấy phần base và technical scaffold.
- Không port nguyên multi-module structure của ThingsBoard.
- Không thêm entity nghiệp vụ thật ngoài các entity kỹ thuật cần cho base như `AdminSettings` và `User`.
- Persistence hiện tại là JPA/SQL only.
- Schema được quản lý bằng Liquibase, không dùng Hibernate auto-create cho môi trường chạy thật.

## Folder Structure
```text
src/main/resources
└── db
    └── changelog
        ├── db.changelog-master.yaml
        └── 001-initial-schema.yaml

src/main/java/com/vtlamdev/sourcebase
├── cache
├── common
│   └── data
│       ├── exception
│       ├── id
│       ├── page
│       └── security
├── config
├── controller
│   └── base
├── dao
│   ├── model
│   │   └── sql
│   ├── repository
│   ├── service
│   │   └── impl
│   └── util
│       └── mapping
├── exception
├── queue
│   └── settings
├── security
├── service
│   └── impl
└── common/util
```

## Package Responsibilities
- `common.data`: base domain model, marker interfaces, core enums.
- `common.data.id`: typed UUID wrapper theo phong cách ThingsBoard.
- `common.data.page`: paging/sorting contract dùng lại cho API và DAO.
- `common.data.security`: authority, password policy, security settings.
- `dao.model` và `dao.model.sql`: base JPA entity, versioned entity, SQL entity kỹ thuật.
- `dao.repository`: Spring Data repository.
- `dao.service`: DAO contract và service contract gần persistence.
- `service`: application service layer để controller gọi vào.
- `controller`: REST entrypoint cho base APIs.
- `config`: security, cache, queue property/config bootstrap.
- `src/main/resources/db/changelog`: nơi quản lý schema migration cho toàn bộ base entity.
- `queue`: queue abstraction để sau này thay bằng Kafka/RabbitMQ/Redis stream hoặc queue riêng.
- `cache`: cache name và cache TTL/property setup.
- `exception`: unified error response và global exception handling.

## Current Base Classes
- ID base: `UUIDBased`, `IdBased`, `EntityId`, `TenantId`, `UserId`, `CustomerId`, `AdminSettingsId`
- Domain base: `BaseData`, `BaseDataWithAdditionalInfo`, `HasTenantId`, `HasCustomerId`, `HasVersion`, `HasName`
- Technical domain: `AdminSettings`, `User`
- JPA base: `BaseEntity`, `BaseSqlEntity`, `BaseVersionedEntity`
- DAO base: `Dao`, `JpaAbstractDao`
- Config base: `SourceBaseSecurityConfiguration`, `SourceBaseCacheConfiguration`, `SourceBaseQueueConfiguration`

## Current Technical APIs
- `GET /api/v1/system/base-info`
- `GET /api/v1/admin/settings/{key}`
- `GET /api/v1/admin/settings`
- `POST /api/v1/admin/settings`
- `DELETE /api/v1/admin/settings/{key}`
- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/auth/token`
- `GET /api/auth/user`
- `POST /api/auth/changePassword`

## How To Extend
### Add a new business entity
1. Tạo typed id trong `common.data.id`.
2. Tạo domain object trong `common.data`.
3. Tạo SQL entity trong `dao.model.sql`.
4. Tạo changeSet Liquibase mới trong `src/main/resources/db/changelog`.
5. Tạo repository trong `dao.repository`.
6. Tạo DAO/service contract trong `dao.service`.
7. Tạo application service trong `service`.
8. Tạo controller trong `controller`.

### Add queue integration
1. Giữ nguyên `QueuePublisher` làm interface nền.
2. Thay `LoggingQueuePublisher` bằng implementation thật.
3. Mở rộng `QueueProperties` cho broker-specific config.

### Add cache for a new entity
1. Thêm cache name vào `cache/CacheNames`.
2. Thêm service-level caching annotation vào application service hoặc DAO service.

## Notes
- Base này đang ưu tiên compile sạch và dễ mở rộng trước.
- Nếu sau này cần đồng bộ sâu hơn với ThingsBoard, nên tiếp tục lấy pattern từ `thingsboard` thay vì từ `source-base-old`.
- Nếu thêm entity thật, nên giữ đúng layering hiện tại để tránh business logic chui thẳng vào controller hoặc repository.
- `spring.jpa.hibernate.ddl-auto` hiện để `validate` cho môi trường chính; test H2 dùng Liquibase để dựng schema và tắt validate do khác biệt mapping `text` giữa H2 và PostgreSQL.
