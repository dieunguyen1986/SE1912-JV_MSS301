package com.talenthub.candidate.domain;

import com.talenthub.web.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * Entity con của Candidate aggregate — metadata file CV đã upload.
 * Sống/chết theo Candidate cha (không có CvFileRepository riêng).
 * Factory `create(...)` để package-private — chỉ Candidate aggregate được tạo.
 */
@Getter
@Entity
@Table(name = "cv_files")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CvFile extends BaseEntity {

    public enum ParseStatus {
        PENDING, PARSED, FAILED
    }

    private static final long MAX_SIZE_BYTES = 5L * 1024 * 1024; // BRULE-08

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "candidate_id", nullable = false)
    private UUID candidateId;

    @Column(name = "file_url", nullable = false, length = 500)
    private String fileUrl;

    @Column(name = "size_bytes", nullable = false)
    private long sizeBytes;

    @Column(name = "uploaded_at", nullable = false)
    private Instant uploadedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "parse_status", nullable = false, length = 20)
    private ParseStatus parseStatus;

    static CvFile create(UUID candidateId, String fileUrl, long sizeBytes) {
        if (candidateId == null) {
            throw new IllegalArgumentException("candidateId required");
        }
        if (fileUrl == null || fileUrl.isBlank()) {
            throw new IllegalArgumentException("fileUrl required");
        }
        if (sizeBytes <= 0) {
            throw new IllegalArgumentException("sizeBytes phải > 0");
        }
        if (sizeBytes > MAX_SIZE_BYTES) {
            throw new IllegalArgumentException("CV vượt quá " + (MAX_SIZE_BYTES / 1024 / 1024) + "MB");
        }
        CvFile file = new CvFile();
        file.candidateId = candidateId;
        file.fileUrl = fileUrl.trim();
        file.sizeBytes = sizeBytes;
        file.uploadedAt = Instant.now();
        file.parseStatus = ParseStatus.PENDING;
        return file;
    }

    void markParsed() {
        if (parseStatus != ParseStatus.PENDING) {
            throw new IllegalStateException("Chỉ PENDING mới mark parsed, hiện tại: " + parseStatus);
        }
        this.parseStatus = ParseStatus.PARSED;
    }

    void markFailed() {
        if (parseStatus != ParseStatus.PENDING) {
            throw new IllegalStateException("Chỉ PENDING mới mark failed, hiện tại: " + parseStatus);
        }
        this.parseStatus = ParseStatus.FAILED;
    }
}
