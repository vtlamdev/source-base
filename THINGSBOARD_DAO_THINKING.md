# ThingsBoard DAO Thinking

## Mục đích

Tài liệu này giải thích vì sao `source-base` dùng các lớp kiểu `Dao<T>`, `JpaAbstractDao`, `TenantEntityDao` thay vì gọi thẳng `JpaRepository` ở service.

## 1. Tách domain ra khỏi JPA entity

- `User`, `Tenant`, `AdminSettings` là domain object.
- `UserEntity`, `TenantEntity`, `AdminSettingsEntity` là persistence object.
- Entity có trách nhiệm map qua lại bằng `toData()` và constructor nhận domain.

Tư duy của ThingsBoard là service làm việc với domain object, không làm việc trực tiếp với JPA entity.

## 2. `Dao<T>` là contract persistence chung

`Dao<T>` gom các thao tác nền:

- `findById`
- `existsById`
- `save`
- `saveAndFlush`
- `removeById`
- `findIdsByTenantIdAndIdOffset`

Nhờ đó service chỉ phụ thuộc vào contract của hệ thống, không phụ thuộc chặt vào Spring Data.

## 3. `JpaAbstractDao` là nơi giữ policy save/load chung

`JpaAbstractDao` xử lý các việc lặp lại:

- convert domain -> entity
- sinh `UUID` nếu chưa có
- set `createdTime`
- set `version`
- map entity -> domain

Lợi ích là mọi entity save theo cùng một rule. Khi cần sửa behavior persistence chung thì sửa một chỗ.

## 4. Capability interface

ThingsBoard không nhét mọi method vào một interface lớn. Họ tách theo khả năng:

- `TenantEntityDao<T>`: entity thuộc tenant, có `countByTenantId`, `findAllByTenantId`
- `ExportableEntityDao<I, T>`: entity có khả năng import/export, external id

Đây là tư duy composition:

- entity thường: chỉ cần `Dao<T>`
- entity thuộc tenant: `Dao<T> + TenantEntityDao<T>`
- entity export/import được: `Dao<T> + ExportableEntityDao<I, T>`

## 5. Ví dụ đang có trong `source-base`

### `User`

- `UserDao` extends `Dao<User>, TenantEntityDao<User>`
- `JpaUserDao` chứa logic tenant-aware đặc thù của user
- `UserServiceImpl` gọi qua `UserDao`, không chạm trực tiếp `UserRepository`

### `AdminSettings`

- `AdminSettingsDao` extends `Dao<AdminSettings>, TenantEntityDao<AdminSettings>`
- `JpaAdminSettingsDao` vừa dùng CRUD chung từ `JpaAbstractDao`, vừa có query riêng theo tenant + key

### `UserCredentials`

- `UserCredentialsDao` chỉ extends `Dao<UserCredentials>`
- lý do: bảng này không phải tenant-owned entity theo đúng nghĩa dữ liệu chính

## 6. Khi nào nên tạo abstraction mới

Chỉ nên thêm interface capability mới nếu có hành vi lặp lại ở nhiều entity.

Ví dụ hợp lý:

- `CustomerEntityDao<T>`
- `VersionedEntityDao<T>`
- `NamedEntityDao<T>`

Ví dụ chưa nên thêm:

- interface chỉ dùng cho đúng 1 entity
- abstraction không có behavior chung, chỉ thêm cho “đủ bộ”

## 7. Nguyên tắc áp dụng cho `source-base`

- Giữ `JpaRepository` là chi tiết hạ tầng.
- Service ưu tiên phụ thuộc vào `*Dao`.
- Logic CRUD lặp lại đưa vào `JpaAbstractDao`.
- Logic tenant-specific để ở DAO capability hoặc concrete DAO.
- Chỉ thêm abstraction khi nó giúp nhiều entity cùng hưởng lợi.
