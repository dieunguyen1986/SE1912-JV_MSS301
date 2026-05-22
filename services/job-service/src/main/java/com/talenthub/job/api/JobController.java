package com.talenthub.job.api;

import com.talenthub.job.api.dto.JobCreatedRequest;
import com.talenthub.job.application.command.DraftJobCommand;
import com.talenthub.job.application.usecase.DraftJobUseCase;
import com.talenthub.job.domain.Job;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(ApiPath.BASE)
@RequiredArgsConstructor
public class JobController {
    private final DraftJobUseCase draftJobUseCase;

    @PostMapping(ApiPath.JOBS)
    public ResponseEntity<?> createJob(@Valid @RequestBody JobCreatedRequest req){
        Job jobRes = draftJobUseCase.execute(new DraftJobCommand(req.getTitle(), req.getDescription(),
                req.getLocation(), req.getDepartmentId(), req.getRequiredSkills(), req.getMinSalary(),
                req.getMaxSalary(), req.getDeadline()));

        return ResponseEntity.ok(Map.of("message", "Create a new job successful",
                "status", HttpStatus.CREATED.value()));
    }
}
