package com.talenthub.job.api.dto;

import com.talenthub.job.domain.Department;

import java.util.UUID;

public record DepartmentResponse(UUID id, String name, UUID managerId) {
    public static DepartmentResponse from(Department d) {
        return new DepartmentResponse(d.getId(), d.getName(), d.getManagerId());
    }
}
