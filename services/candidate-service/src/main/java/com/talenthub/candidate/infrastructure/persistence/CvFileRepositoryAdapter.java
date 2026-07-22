package com.talenthub.candidate.infrastructure.persistence;

import com.talenthub.candidate.domain.model.CvFile;
import com.talenthub.candidate.domain.repository.CvFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CvFileRepositoryAdapter implements CvFileRepository {
    private final CvFileJpaRepository cvFileJpaRepository;


    @Override
    public CvFile save(CvFile cvFile) {
        return cvFileJpaRepository.save(cvFile);
    }
}
