package com.talenthub.candidate.infrastructure.storage;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;

/**
 * Mock implementation — chưa upload lên S3/MinIO ở stage này (xem stage sau).
 * Sinh URL giả `mock://candidates/{id}/{filename}` để Candidate aggregate validate
 * và lưu metadata. Stage tiếp theo thay bằng adapter S3 thật.
 */
@Component
public class MockCvStorageService implements CvStorageService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );

    @Override
    public StoredFile store(UUID candidateId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("CV file là bắt buộc");
        }
        String contentType = file.getContentType();
        if (contentType != null && !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Định dạng CV không hỗ trợ: " + contentType);
        }
        String filename = file.getOriginalFilename() == null ? "cv.pdf" : file.getOriginalFilename();
        String url = "mock://candidates/" + candidateId + "/" + filename;
        return new StoredFile(url, file.getSize());
    }
}
