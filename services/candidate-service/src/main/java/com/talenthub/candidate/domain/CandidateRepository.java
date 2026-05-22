package com.talenthub.candidate.domain;

import java.util.Optional;
import java.util.UUID;

public interface CandidateRepository {
    Candidate save(Candidate candidate);
    Optional<Candidate> findById(UUID id);
    boolean existsByEmail(String email);
}
