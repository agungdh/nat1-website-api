# AGENTS.md — nat1_website_api

## Tech Stack
- Java 25, Spring Boot 4.0.7, Gradle
- PostgreSQL 18, Redis
- JPA / Hibernate, MapStruct 1.6.3, Lombok, SpringDoc OpenAPI 3.0.2

## Project Conventions

### Entity (`entity/`)

- Semua entity **wajib extends `BaseEntity`**
- `Long id` — primary key, **tidak pernah di-expose ke FE** (tidak muncul di DTO/JSON)
- `String uuid` — UUID v4, external identifier untuk FE, **indexed tapi tidak unique**
- Audit columns (semua nullable):

| Field | Type | DB Column | Keterangan |
|---|---|---|---|
| `createdAt` | `Long` | `created_at` (BIGINT) | Epoch millis |
| `createdBy` | `Long` | `created_by` (BIGINT) | User ID |
| `updatedAt` | `Long` | `updated_at` (BIGINT) | Epoch millis |
| `updatedBy` | `Long` | `updated_by` (BIGINT) | User ID |
| `deletedAt` | `Long` | `deleted_at` (BIGINT) | Epoch millis |
| `deletedBy` | `Long` | `deleted_by` (BIGINT) | User ID |

- UUID di-generate otomatis via `@PrePersist`
- `createdAt`/`updatedAt` di-set otomatis via JPA Auditing (`DateTimeProvider` epoch millis)
- `createdBy`/`updatedBy` akan diisi otomatis setelah auth system ada (`AuditorAware`)

### Soft Delete

- Semua query **harus** filter `deleted_at IS NULL`
- Hibernate `@Where(clause = "deleted_at IS NULL")` pada entity
- Jangan delete fisik, gunakan `softDelete(Long deletedBy)` di `BaseEntity`

### FK / Relasi

- **Database level**: FK tetap pakai `Long id` (referential integrity, performa)
- **API level**: FE kirim/terima UUID, service layer translate UUID → ID internal
- Kolom FK di entity tetap `_id` (Long), bukan `_uuid`

### DTO (`dto/`)

- Pakai Java `record` (immutable)
- Hanya berisi `uuid` (tidak ada `id`)
- Naming convention:
  - `{Entity}Dto` — response (read)
  - `{Entity}CreateDto` — request (create)
  - `{Entity}UpdateDto` — request (update)

### MapStruct Mapper (`mapper/`)

```java
@Mapper(componentModel = "spring")
public interface FooMapper {
    FooDto toDto(Foo entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    Foo toEntity(FooCreateDto dto);
}
```

## Notes
- JPA config di `config/JpaConfig.java` — `@EnableJpaAuditing`, `AuditorAware<Long>`, `DateTimeProvider` epoch millis
- UUID index dibuat via `@Index` di `@Table` tiap subclass entity (tidak di `BaseEntity` karena `@MappedSuperclass`)
- `spring.jpa.hibernate.ddl-auto: update` — schema auto-generated dari entity
