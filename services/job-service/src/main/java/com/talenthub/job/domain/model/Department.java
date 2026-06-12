package com.talenthub.job.domain.model;

import com.talenthub.web.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
@Table(name = "departments")
@SQLRestriction("is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Department extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150, unique = true)
    private String name;

    @Column(name = "manager_id")
    private UUID managerId;

    @OneToMany(mappedBy = "department")
    private Set<Job> jobs = new HashSet<Job>();

    public static Department getInstance(UUID id) {
        Department department = new Department();
        department.id = id;

        return department;
    }

    public static Department create(String name, UUID managerId) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Department name must not be blank");
        }
        Department dept = new Department();
        dept.id = UUID.randomUUID();
        dept.name = name.trim();
        dept.managerId = managerId;
        return dept;
    }

    public void assignManager(UUID newManagerId) {
        if (newManagerId == null) {
            throw new IllegalArgumentException("Manager id must not be null");
        }
        this.managerId = newManagerId;
    }

    public void rename(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Department name must not be blank");
        }
        this.name = newName.trim();
    }
}
