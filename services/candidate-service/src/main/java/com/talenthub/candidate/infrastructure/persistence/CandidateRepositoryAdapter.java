package com.talenthub.candidate.infrastructure.persistence;

import com.talenthub.candidate.domain.Candidate;
import com.talenthub.candidate.domain.CandidateRepository;
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
    public Optional<Candidate> findByEmail(String email) {
        return candidateJpaRepository.findByContactEmail(email == null ? null : email.trim().toLowerCase());
    }

    @Override
    public boolean existsByEmail(String email) {
        return candidateJpaRepository.existsByContactEmail(email == null ? null : email.trim().toLowerCase());
    }

    @Override
    public Page<Candidate> search(String keyword, Pageable pageable) {
        return candidateJpaRepository.search(keyword, pageable);
    }
}
