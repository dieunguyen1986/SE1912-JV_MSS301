package com.talenthub.job.domain.repository;

import com.talenthub.job.domain.model.Department;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DepartmentRepository {
    Department save(Department department);

    Optional<Department> findById(UUID id);

    List<Department> findAll();

    boolean existsByName(String name);
}
