package com.talenthub.application.application.usecase;

import com.talenthub.application.domain.model.Application;
import com.talenthub.application.domain.model.PipelineStage;
import com.talenthub.application.domain.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/** UC-02.7b — Filter Kanban by Job (và/hoặc theo stage). */
@Service
@RequiredArgsConstructor
public class ListApplicationsUseCase {

    private final ApplicationRepository repo;

    @Transactional(readOnly = true)
    public Page<Application> execute(UUID jobId, PipelineStage stage, Pageable pageable) {
        return repo.search(jobId, stage, pageable);
    }
}
