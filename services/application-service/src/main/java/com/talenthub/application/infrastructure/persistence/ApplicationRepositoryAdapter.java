package com.talenthub.application.infrastructure.persistence;

import com.talenthub.application.domain.model.Application;
import com.talenthub.application.domain.model.PipelineStage;
import com.talenthub.application.domain.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ApplicationRepositoryAdapter implements ApplicationRepository {
    private final ApplicationJpaRepository appJpaRepository;
    @Override
    public Application save(Application application) {
        return appJpaRepository.save(application);
    }

    @Override
    public Optional<Application> findById(UUID id) {
        return appJpaRepository.findById(id);
    }

    @Override
    public boolean existsByCandidateIdAndJobId(UUID candidateId, UUID jobId) {
        return appJpaRepository.existsByCandidateIdAndJobId(candidateId, jobId);
    }

    @Override
    public Page<Application> search(UUID jobId, PipelineStage stage, Pageable pageable) {
        return null;
    }
}
