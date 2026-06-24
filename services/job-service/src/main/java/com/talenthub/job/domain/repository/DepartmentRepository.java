package com.talenthub.job.domain.repository;

import com.talenthub.job.domain.aggregate.DepartmentAggregate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DepartmentRepository {

    DepartmentAggregate save(DepartmentAggregate aggregate);

    Optional<DepartmentAggregate> findById(UUID id);

    List<DepartmentAggregate> findAll();

    boolean existsByName(String name);

    void delete(UUID id);
}
