package com.talenthub.application.infrastructure.persistence;

import com.talenthub.application.domain.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ApplicationJpaRepository extends JpaRepository<Application, UUID> {
    boolean existsByCandidateIdAndJobId(UUID candidateId, UUID jobId);
}