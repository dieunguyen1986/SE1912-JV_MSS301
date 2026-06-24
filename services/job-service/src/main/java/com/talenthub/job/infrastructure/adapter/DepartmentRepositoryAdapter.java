package com.talenthub.job.infrastructure.adapter;

import com.talenthub.job.domain.aggregate.DepartmentAggregate;
import com.talenthub.job.domain.repository.DepartmentRepository;
import com.talenthub.job.infrastructure.persistence.DepartmentJpaRepository;
import com.talenthub.job.infrastructure.persistence.entity.Department;
import com.talenthub.job.infrastructure.persistence.mapper.DepartmentPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DepartmentRepositoryAdapter implements DepartmentRepository {

    private final DepartmentJpaRepository departmentJpaRepository;

    @Override
    public DepartmentAggregate save(DepartmentAggregate aggregate) {
        Department entity;
        if (aggregate.getId() == null) {
            entity = DepartmentPersistenceMapper.toNewEntity(aggregate);
        } else {
            entity = departmentJpaRepository.findById(aggregate.getId())
                    .orElseGet(() -> DepartmentPersistenceMapper.toNewEntity(aggregate));
            DepartmentPersistenceMapper.applyToEntity(aggregate, entity);
        }
        return DepartmentPersistenceMapper.toAggregate(departmentJpaRepository.save(entity));
    }

    @Override
    public Optional<DepartmentAggregate> findById(UUID id) {
        return departmentJpaRepository.findById(id).map(DepartmentPersistenceMapper::toAggregate);
    }

    @Override
    public List<DepartmentAggregate> findAll() {
        return departmentJpaRepository.findAll().stream()
                .map(DepartmentPersistenceMapper::toAggregate)
                .toList();
    }

    @Override
    public boolean existsByName(String name) {
        return departmentJpaRepository.existsByName(name);
    }

    @Override
    public void delete(UUID id) {
        departmentJpaRepository.findById(id).ifPresent(entity -> {
            entity.softDelete();
            departmentJpaRepository.save(entity);
        });
    }
}
