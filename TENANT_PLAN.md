# Tenant Design Plan

## Mục Đích
Tài liệu này mô tả thiết kế đề xuất cho `Tenant` trong `source-base` để review trước khi implement.

Mục tiêu của `Tenant`:
- đại diện cho một tổ chức hoặc không gian dữ liệu cấp cao
- làm root owner cho user, customer, device, asset, settings và dữ liệu nghiệp vụ khác
- tạo nền cho multi-tenant đúng kiểu ThingsBoard, nhưng vẫn giữ source ở dạng single-module

## Trạng Thái Hiện Tại
Hiện tại `source-base` mới có:
- `TenantId`
- `User.tenantId`
- `AdminSettings.tenantId`

Hiện tại chưa có:
- class domain `Tenant`
- `TenantEntity`
- bảng `tenant`
- repository/dao/service/controller cho tenant
- foreign key từ `tb_user.tenant_id` sang bảng `tenant`

Nghĩa là ở thời điểm này `tenantId` mới là reference field, chưa phải relation DB hoàn chỉnh.

## Vai Trò Của Tenant
`Tenant` không phải tài khoản đăng nhập.

`Tenant` là:
- đơn vị sở hữu dữ liệu
- boundary phân vùng dữ liệu
- root để gom user và tài nguyên nghiệp vụ

`User` là:
- tài khoản con người dùng để login
- thuộc về một `Tenant`
- có quyền như `TENANT_ADMIN`, `CUSTOMER_USER`, hoặc đặc biệt là `SYS_ADMIN`

Ví dụ:
- Tenant: `ACME Corp`
- Users:
  - `admin@acme.com`
  - `ops@acme.com`
  - `viewer@acme.com`

## Định Hướng Thiết Kế
Theo hướng ThingsBoard và phù hợp với `source-base`, nên:
- ưu tiên entity phẳng
- dùng `UUID` reference thay vì `@OneToMany/@ManyToOne` mặc định
- service layer xử lý load/join rõ ràng khi cần
- chỉ thêm JPA relation nếu có lý do rất mạnh

Nói ngắn gọn:
- `Tenant` là root aggregate logic
- nhưng ở JPA layer vẫn nên giữ mapping mỏng

## Đề Xuất Domain Model
### Class
Đề xuất thêm:
- `common.data.Tenant`
- `common.data.id.TenantProfileId` nếu sau này tách profile/settings riêng

### Tenant fields
Đề xuất field cho `Tenant`:

1. `id: TenantId`
- khóa chính
- UUID

2. `createdTime: long`
- thời điểm tạo

3. `title: String`
- tên hiển thị của tenant
- ví dụ: `ACME Corp`

4. `region: String`
- vùng hoặc khu vực hoạt động
- optional

5. `country: String`
- quốc gia
- optional

6. `state: String`
- trạng thái tenant
- ví dụ: `ACTIVE`, `SUSPENDED`
- có thể dùng enum sau

7. `email: String`
- email liên hệ chính của tenant
- không phải email login

8. `phone: String`
- số điện thoại liên hệ

9. `address: String`
- địa chỉ liên hệ

10. `additionalInfo: JsonNode`
- metadata mở rộng
- ví dụ:
  - timezone
  - industry
  - billing metadata
  - notes

11. `version: Long`
- optimistic locking giống `User`

## Đề Xuất Enum
### TenantState
Đề xuất enum:
- `ACTIVE`
- `SUSPENDED`
- `DELETED`

Mục đích:
- `ACTIVE`: tenant hoạt động bình thường
- `SUSPENDED`: khóa tạm, user tenant không dùng được
- `DELETED`: soft-delete nếu sau này cần

## Đề Xuất Database Schema
### Table
Đề xuất bảng:
- `tenant`

### Columns
Đề xuất cột:
- `id uuid primary key`
- `created_time bigint not null`
- `version bigint`
- `title varchar(255) not null`
- `region varchar(255)`
- `country varchar(255)`
- `state varchar(32) not null`
- `email varchar(255)`
- `phone varchar(255)`
- `address varchar(512)`
- `additional_info text`

### Constraints
Đề xuất:
- primary key: `id`
- unique index cho `title` nếu muốn title là duy nhất toàn hệ thống
- hoặc bỏ unique `title` nếu muốn cho phép trùng tên tenant

Theo tôi:
- chưa nên unique `title` ở base
- chỉ cần index nếu sau này có search

### Foreign Keys
Sau khi có bảng `tenant`, nên cập nhật:

1. `tb_user.tenant_id -> tenant.id`
- áp dụng cho user tenant thông thường

2. `admin_settings.tenant_id -> tenant.id`
- cần cân nhắc riêng cho `SYS_TENANT_ID`

### Lưu Ý Về SYS_TENANT_ID
Hiện source đang có:
- `TenantId.SYS_TENANT_ID = NULL_UUID`

Có 2 hướng:

1. Giữ nguyên style ThingsBoard:
- `SYS_TENANT_ID` là UUID đặc biệt `00000000-0000-0000-0000-000000000000`
- không nhất thiết có row tenant thật cho sys tenant

2. Tạo row tenant thật cho sys tenant:
- vẫn dùng đúng `NULL_UUID`
- insert sẵn một bản ghi `System Tenant`

Theo tôi với `source-base` nên chọn hướng 2:
- dễ làm foreign key hơn
- schema rõ ràng hơn
- data nhất quán hơn

## Quan Hệ Với Các Entity Khác
### User -> Tenant
Quan hệ logic:
- nhiều `User` thuộc một `Tenant`

Quan hệ ở DB:
- `tb_user.tenant_id` là FK tới `tenant.id`

Quan hệ ở JPA:
- không bắt buộc dùng `@ManyToOne`
- giữ `tenantId: UUID` trong `UserEntity` là đủ

### AdminSettings -> Tenant
Quan hệ logic:
- một `Tenant` có nhiều `AdminSettings`

Quan hệ ở DB:
- `admin_settings.tenant_id` là FK tới `tenant.id`

Quan hệ ở JPA:
- nên giữ reference id, không cần object graph

### Customer -> Tenant
Nếu sau này thêm `Customer`, quan hệ sẽ là:
- nhiều `Customer` thuộc một `Tenant`

### Device/Asset/Dashboard/... -> Tenant
Hầu hết resource nghiệp vụ sau này đều nên có:
- `tenantId`

## API Đề Xuất
Base API nên đủ cho CRUD và review tenant context.

### 1. Create Tenant
`POST /api/v1/tenants`

Mục đích:
- tạo tenant mới

Request body đề xuất:
```json
{
  "title": "ACME Corp",
  "region": "APAC",
  "country": "VN",
  "state": "ACTIVE",
  "email": "contact@acme.com",
  "phone": "0123456789",
  "address": "Ho Chi Minh City",
  "additionalInfo": {
    "timezone": "Asia/Ho_Chi_Minh"
  }
}
```

Response:
- trả về `Tenant`

Validation:
- `title` bắt buộc
- `state` bắt buộc
- email nếu có thì đúng format

Ai được gọi:
- `SYS_ADMIN`

### 2. Update Tenant
`POST /api/v1/tenants/{tenantId}`

Mục đích:
- cập nhật thông tin tenant

Request body:
- giống `create`, nhưng có thể gửi đầy đủ object tenant

Response:
- tenant sau khi update

Ai được gọi:
- `SYS_ADMIN`

### 3. Get Tenant By Id
`GET /api/v1/tenants/{tenantId}`

Mục đích:
- xem chi tiết 1 tenant

Response:
- object `Tenant`

Ai được gọi:
- `SYS_ADMIN`
- hoặc `TENANT_ADMIN` nếu xem chính tenant của mình, tùy policy

### 4. List Tenants
`GET /api/v1/tenants?pageSize=20&page=0&textSearch=acme`

Mục đích:
- phân trang danh sách tenant

Query params:
- `pageSize`
- `page`
- `textSearch`

Response:
```json
{
  "data": [],
  "totalPages": 0,
  "totalElements": 0,
  "hasNext": false
}
```

Ai được gọi:
- `SYS_ADMIN`

### 5. Delete Tenant
`DELETE /api/v1/tenants/{tenantId}`

Mục đích:
- xóa tenant

Có 2 hướng:

1. hard delete
- xóa thật
- rủi ro cao nếu tenant đã có nhiều dữ liệu

2. soft delete / suspend
- đổi trạng thái
- an toàn hơn

Theo tôi:
- base source nên chưa hard delete ngay
- nên ưu tiên `suspend` hoặc `mark deleted`

Ai được gọi:
- `SYS_ADMIN`

### 6. Get Current Tenant
`GET /api/v1/tenants/me`

Mục đích:
- trả về tenant của user hiện tại

Response:
- object `Tenant`

Ai được gọi:
- user đã đăng nhập

### 7. Change Tenant State
`POST /api/v1/tenants/{tenantId}/state`

Mục đích:
- đổi trạng thái tenant nhanh mà không cần update toàn bộ

Request body:
```json
{
  "state": "SUSPENDED"
}
```

Response:
- tenant sau khi update

Ai được gọi:
- `SYS_ADMIN`

## Service Layer Đề Xuất
### Domain / DAO
Đề xuất thêm:
- `dao.model.sql.TenantEntity`
- `dao.repository.TenantRepository`
- `dao.service.TenantDao`
- `dao.service.TenantService`
- `dao.service.impl.JpaTenantDao`
- `dao.service.impl.TenantServiceImpl`

### Application Service
Đề xuất thêm:
- `service.TenantApplicationService`
- `service.impl.TenantApplicationServiceImpl`

Mục đích:
- tách persistence concern và business concern

## Security Rule Đề Xuất
### SYS_ADMIN
Được phép:
- create tenant
- update tenant
- list tenant
- change state
- suspend/delete tenant

### TENANT_ADMIN
Được phép:
- xem tenant của chính mình
- update một phần thông tin tenant của mình nếu business cho phép

### CUSTOMER_USER
Thường:
- không thao tác tenant
- chỉ đọc context nếu cần

## Validation Đề Xuất
### Create / Update
Nên validate:
- `title` không rỗng
- `state` hợp lệ
- `email` đúng format nếu có
- `phone` giới hạn độ dài
- `address` giới hạn độ dài

### Delete / Suspend
Nên kiểm tra:
- tenant có phải `SYS_TENANT_ID` không
- có được phép xóa/suspend system tenant không

Theo tôi:
- không cho xóa `SYS_TENANT_ID`

## Seed Data Đề Xuất
Khi implement tenant thật, nên seed:

1. `System Tenant`
- `id = 00000000-0000-0000-0000-000000000000`
- `title = System Tenant`
- `state = ACTIVE`

2. `sysadmin@sourcebase.local`
- tiếp tục thuộc `System Tenant`

Mục đích:
- nhất quán với auth hiện tại
- dễ làm foreign key

## Liquibase Đề Xuất
Khi implement, nên thêm changelog mới:
- `002-tenant-schema.yaml`

Nội dung:
- create table `tenant`
- insert `System Tenant`
- add FK từ `tb_user.tenant_id`
- add FK từ `admin_settings.tenant_id`

Nếu cần migration an toàn hơn:
- thêm cột nullable trước
- backfill data
- sau đó mới add FK / not null constraint

## Đề Xuất Thứ Tự Implement
1. tạo `Tenant` domain + `TenantEntity`
2. tạo Liquibase schema `tenant`
3. seed `System Tenant`
4. thêm FK vào `tb_user` và `admin_settings`
5. tạo repository/dao/service
6. tạo controller CRUD cơ bản
7. thêm security rules
8. thêm test cho create/get/list/current tenant

## Kết Luận
Theo tôi, `Tenant` nên là bước tiếp theo rất hợp lý cho `source-base` vì:
- hiện auth đã có `tenantId` nhưng mới là reference field
- thêm tenant thật sẽ làm schema và domain nhất quán hơn
- sau này mở rộng customer/device/asset sẽ dễ hơn nhiều

Nếu triển khai, tôi khuyến nghị:
- giữ entity phẳng
- không lạm dụng JPA relation
- seed `System Tenant`
- dùng Liquibase để thêm bảng và FK
- cho CRUD tenant đi trước, xóa cứng đi sau
