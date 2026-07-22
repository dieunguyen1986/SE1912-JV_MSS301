package com.talenthub.candidate.application.usecase;

import com.talenthub.candidate.domain.model.Candidate;
import com.talenthub.candidate.domain.repository.CandidateRepository;
import com.talenthub.candidate.domain.exception.CandidateNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteCandidateUseCase {

    private final CandidateRepository candidateRepository;

    @Transactional
    public void execute(UUID id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new CandidateNotFoundException(id));

        candidate.softDelete();
    }
}
