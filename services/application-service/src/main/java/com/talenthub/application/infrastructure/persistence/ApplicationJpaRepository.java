package com.talenthub.application.infrastructure.persistence;

import com.talenthub.application.domain.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ApplicationJpaRepository
        extends JpaRepository<Application, UUID>, JpaSpecificationExecutor<Application> {
    boolean existsByCandidateIdAndJobId(UUID candidateId, UUID jobId);
}