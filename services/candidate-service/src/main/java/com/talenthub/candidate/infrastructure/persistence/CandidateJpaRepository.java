package com.talenthub.candidate.infrastructure.persistence;

import com.talenthub.candidate.domain.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CandidateJpaRepository extends JpaRepository<Candidate, UUID> {
    boolean existsByContactEmail(String email);

}
