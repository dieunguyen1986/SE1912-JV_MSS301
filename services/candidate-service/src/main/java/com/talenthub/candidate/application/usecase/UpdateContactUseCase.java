package com.talenthub.candidate.application.usecase;

import com.talenthub.candidate.application.command.UpdateContactCommand;
import com.talenthub.candidate.domain.model.Candidate;
import com.talenthub.candidate.domain.repository.CandidateRepository;
import com.talenthub.candidate.domain.model.ContactInfo;
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
    public void execute(UpdateContactCommand command) {
        Candidate candidate = candidateRepository.findById(command.candidateId())
                .orElseThrow(() -> new CandidateNotFoundException(command.candidateId()));

        ContactInfo newContact = new ContactInfo(command.email(), command.phone(), command.address());

        boolean emailChanged = !candidate.getContact().email().equals(newContact.email());
        if (emailChanged && candidateRepository.existsByEmail(newContact.email())) {
            throw new DuplicateEmailException(newContact.email());
        }

        candidate.updateContact(newContact);
    }
}
