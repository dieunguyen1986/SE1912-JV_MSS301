package com.talenthub.application.domain.repository;

import com.talenthub.application.domain.model.Application;
import com.talenthub.application.domain.model.PipelineStage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ApplicationRepository {
    Application save(Application application);

    Optional<Application> findById(UUID id);

    boolean existsByCandidateIdAndJobId(UUID candidateId, UUID jobId);  // BRULE-09

    /**
     * Phục vụ Kanban board: lọc theo job và/hoặc stage (cả hai optional).
     */
    Page<Application> search(UUID jobId, PipelineStage stage, Pageable pageable);
}
