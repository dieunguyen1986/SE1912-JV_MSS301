package com.talenthub.candidate.application.usecase;

import com.talenthub.candidate.domain.model.Candidate;
import com.talenthub.candidate.domain.repository.CandidateRepository;
import com.talenthub.candidate.domain.model.ParsedCvData;
import com.talenthub.candidate.domain.exception.CandidateNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompleteParsingUseCase {
    private final CandidateRepository candidateRepository;

    @Transactional
    public Candidate execute(UUID candidateId, ParsedCvData data) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new CandidateNotFoundException(candidateId));

        candidate.completeParsing(data);
        return candidateRepository.save(candidate);
    }
}
