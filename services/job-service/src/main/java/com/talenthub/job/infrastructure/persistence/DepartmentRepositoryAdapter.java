package com.talenthub.job.infrastructure.persistence;

import com.talenthub.job.domain.model.Department;
import com.talenthub.job.domain.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DepartmentRepositoryAdapter implements DepartmentRepository {
    private final DepartmentJpaRepository jpa;

    @Override
    public Department save(Department department) {
        return jpa.save(department);
    }

    @Override
    public Optional<Department> findById(UUID id) {
        return jpa.findById(id);
    }

    @Override
    public List<Department> findAll() {
        return jpa.findAll();
    }

    @Override
    public boolean existsByName(String name) {
        return jpa.existsByName(name);
    }
}
