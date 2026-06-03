package com.talenthub.candidate.application.usecase;

import com.talenthub.candidate.domain.Candidate;
import com.talenthub.candidate.domain.CandidateRepository;
import com.talenthub.candidate.domain.exception.CandidateNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetCandidateUseCase {
    private final CandidateRepository candidateRepository;

    @Transactional(readOnly = true)
    public Candidate byId(UUID id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new CandidateNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Candidate byEmail(String email) {
        return candidateRepository.findByEmail(email)
                .orElseThrow(() -> new CandidateNotFoundException(email));
    }
}
