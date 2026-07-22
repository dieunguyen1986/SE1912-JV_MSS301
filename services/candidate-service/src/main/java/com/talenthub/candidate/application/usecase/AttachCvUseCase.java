package com.talenthub.candidate.application.usecase;

import com.talenthub.candidate.application.command.AttachCvCommand;
import com.talenthub.candidate.domain.exception.CandidateNotFoundException;
import com.talenthub.candidate.domain.model.Candidate;
import com.talenthub.candidate.domain.model.CvFile;
import com.talenthub.candidate.domain.repository.CandidateRepository;
import com.talenthub.candidate.domain.repository.CvFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AttachCvUseCase {

    private final CandidateRepository candidateRepository;
    private final FileStorageService fileStorageService;
    private final CvFileRepository cvFileRepository;

    @Transactional
    public String execute(AttachCvCommand command) {
        Candidate candidate = candidateRepository.findById(command.candidateId())
                .orElseThrow(() -> new CandidateNotFoundException(command.candidateId()));

        String fileUrl = fileStorageService.uploadFile(command.file());

        // Gắn CV vào Candidate
        candidate.attachCv(fileUrl, command.file().getSize());
        
        // Lưu lại Candidate, Hibernate sẽ tự động lưu kèm CvFile nhờ CascadeType.ALL
        candidateRepository.save(candidate);

        return fileUrl;
    }
}
