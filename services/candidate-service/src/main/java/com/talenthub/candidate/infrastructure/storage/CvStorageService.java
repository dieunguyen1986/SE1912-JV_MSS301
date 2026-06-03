package com.talenthub.candidate.infrastructure.storage;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface CvStorageService {
    StoredFile store(UUID candidateId, MultipartFile file);

    record StoredFile(String url, long sizeBytes) {
    }
}
