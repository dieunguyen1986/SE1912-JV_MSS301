package com.talenthub.candidate.domain.repository;

import com.talenthub.candidate.domain.model.CvFile;

public interface CvFileRepository {
    CvFile save(CvFile cvFile);
}
