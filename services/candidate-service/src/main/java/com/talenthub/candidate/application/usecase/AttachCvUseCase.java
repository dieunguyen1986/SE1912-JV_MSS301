package com.talenthub.candidate.application.usecase;

import com.talenthub.candidate.application.command.AttachCvCommand;
import com.talenthub.candidate.domain.model.Candidate;
import com.talenthub.candidate.domain.CandidateRepository;
import com.talenthub.candidate.domain.exception.CandidateNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AttachCvUseCase {

    private final CandidateRepository candidateRepository;

    @Transactional
    public void execute(AttachCvCommand command) {
        Candidate candidate = candidateRepository.findById(command.candidateId())
                .orElseThrow(() -> new CandidateNotFoundException(command.candidateId()));

        candidate.attachCv(command.fileUrl(), command.sizeBytes());
    }
}
