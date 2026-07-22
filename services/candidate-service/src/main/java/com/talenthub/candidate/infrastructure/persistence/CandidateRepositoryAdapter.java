package com.talenthub.candidate.infrastructure.persistence;

import com.talenthub.candidate.domain.model.Candidate;
import com.talenthub.candidate.domain.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CandidateRepositoryAdapter implements CandidateRepository {
    private final CandidateJpaRepository candidateJpaRepository;

    @Override
    public Candidate save(Candidate candidate) {
        return candidateJpaRepository.save(candidate);
    }

    @Override
    public Optional<Candidate> findById(UUID id) {
        return candidateJpaRepository.findById(id);
    }

    @Override
    public Page<Candidate> findAll(Pageable pageable) {
        return candidateJpaRepository.findAll(pageable);
    }

    @Override
    public boolean existsByEmail(String email) {
        if (email == null) return false;
        return candidateJpaRepository.existsByContactEmail(email.trim().toLowerCase());
    }
}
