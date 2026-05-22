package com.talenthub.web.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @CreatedDate // // ← JPA tự điền khi INSERT
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate // ← JPA tự điền khi UPDATE
    @Column(name = "updated_at")
    private Instant updatedAt;

    @CreatedBy  // ← JPA tự điền từ AuditorAware
    @Column(name = "created_by", length = 255, updatable = false)
    private String createdBy;

    @LastModifiedBy // ← JPA tự điền từ AuditorAware
    @Column(name = "updated_by", length = 255)
    private String updatedBy;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    public void softDelete() {
        this.deleted = true;
        this.deletedAt = Instant.now();
    }
}
