package com.talenthub.job.application.usecase;

import com.talenthub.job.domain.exception.JobNotFoundException;
import com.talenthub.job.domain.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteJobUseCase {

    private final JobRepository jobRepository;

    @Transactional
    public void execute(UUID id) {
        if (jobRepository.findById(id).isEmpty()) {
            throw new JobNotFoundException(id);
        }
        jobRepository.delete(id);
    }
}
