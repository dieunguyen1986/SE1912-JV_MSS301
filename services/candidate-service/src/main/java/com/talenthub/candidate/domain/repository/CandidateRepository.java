package com.talenthub.candidate.domain.repository;

import com.talenthub.candidate.domain.model.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CandidateRepository {
    Candidate save(Candidate candidate);
    Optional<Candidate> findById(UUID id);
    Page<Candidate> findAll(Pageable pageable);
    boolean existsByEmail(String email);
}
