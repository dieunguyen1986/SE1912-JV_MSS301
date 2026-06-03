package com.talenthub.candidate.application.usecase;

import com.talenthub.candidate.application.command.UpdateContactCommand;
import com.talenthub.candidate.domain.Candidate;
import com.talenthub.candidate.domain.CandidateRepository;
import com.talenthub.candidate.domain.ContactInfo;
import com.talenthub.candidate.domain.exception.CandidateNotFoundException;
import com.talenthub.candidate.domain.exception.DuplicateEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateContactUseCase {
    private final CandidateRepository candidateRepository;

    @Transactional
    public Candidate execute(UpdateContactCommand command) {
        Candidate candidate = candidateRepository.findById(command.candidateId())
                .orElseThrow(() -> new CandidateNotFoundException(command.candidateId()));

        String newEmail = command.email() == null ? null : command.email().trim().toLowerCase();
        boolean emailChanged = newEmail != null
                && (candidate.getContact() == null || !newEmail.equals(candidate.getContact().email()));
        if (emailChanged && candidateRepository.existsByEmail(newEmail)) {
            throw new DuplicateEmailException(newEmail);
        }

        candidate.updateContact(new ContactInfo(command.email(), command.phone(), command.address()));
        return candidateRepository.save(candidate);
    }
}
