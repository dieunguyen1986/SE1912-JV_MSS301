package com.talenthub.job.infrastructure.persistence;

import com.talenthub.job.infrastructure.persistence.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DepartmentJpaRepository extends JpaRepository<Department, UUID> {
    boolean existsByName(String name);
}
