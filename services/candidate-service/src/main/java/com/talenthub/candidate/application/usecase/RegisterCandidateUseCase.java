package com.talenthub.candidate.application.usecase;

import com.talenthub.candidate.application.command.RegisterCandidateCommand;
import com.talenthub.candidate.domain.Candidate;
import com.talenthub.candidate.domain.CandidateRepository;
import com.talenthub.candidate.domain.ContactInfo;
import com.talenthub.candidate.domain.exception.DuplicateEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterCandidateUseCase {

    private final CandidateRepository candidateRepository;

    public UUID execute(RegisterCandidateCommand command) {
        if(candidateRepository.existsByEmail(command.email())){
            throw new DuplicateEmailException(command.email());
        }

        ContactInfo contactInfo = new ContactInfo(command.email(), command.phone(), command.address());

        Candidate candidate = Candidate.register(command.fullName(), contactInfo);

        return candidateRepository.save(candidate).getId();
    }
}
