package id.my.agungdh.nat1_website_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 36, nullable = false)
    private String uuid;

    @Column
    private Long createdAt;

    @Column
    private Long createdBy;

    @Column
    private Long updatedAt;

    @Column
    private Long updatedBy;

    @Column
    private Long deletedAt;

    @Column
    private Long deletedBy;

    @PrePersist
    protected void prePersist() {
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
        }
        if (createdAt == null) {
            createdAt = Instant.now().toEpochMilli();
        }
    }

    @PreUpdate
    protected void preUpdate() {
        updatedAt = Instant.now().toEpochMilli();
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public void softDelete(Long deletedBy) {
        this.deletedAt = Instant.now().toEpochMilli();
        this.deletedBy = deletedBy;
    }
}
