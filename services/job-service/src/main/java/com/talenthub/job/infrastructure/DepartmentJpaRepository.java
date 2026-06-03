package com.talenthub.job.infrastructure;

import com.talenthub.job.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DepartmentJpaRepository extends JpaRepository<Department, UUID> {
    boolean existsByName(String name);
}
