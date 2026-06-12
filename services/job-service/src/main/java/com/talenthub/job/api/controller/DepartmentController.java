package com.talenthub.job.api.controller;

import com.talenthub.job.api.dto.CreateDepartmentRequest;
import com.talenthub.job.api.dto.DepartmentResponse;
import com.talenthub.job.domain.model.Department;
import com.talenthub.job.domain.repository.DepartmentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiPath.BASE + "/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentRepository departmentRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DepartmentResponse> create(@Valid @RequestBody CreateDepartmentRequest req) {
        if (departmentRepository.existsByName(req.name())) {
            throw new IllegalArgumentException("Department name đã tồn tại: " + req.name());
        }
        Department saved = departmentRepository.save(Department.create(req.name(), req.managerId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(DepartmentResponse.from(saved));
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<DepartmentResponse> list() {
        return departmentRepository.findAll().stream().map(DepartmentResponse::from).toList();
    }
}
