package com.talenthub.candidate.application.usecase;

import com.talenthub.candidate.domain.model.Candidate;
import com.talenthub.candidate.domain.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListCandidatesUseCase {

    private final CandidateRepository candidateRepository;

    @Transactional(readOnly = true)
    public Page<Candidate> execute(Pageable pageable) {
        return candidateRepository.findAll(pageable);
    }
}
