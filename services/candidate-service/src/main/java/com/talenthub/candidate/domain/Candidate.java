package com.talenthub.candidate.domain;

import com.talenthub.web.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

/**
 * Aggregate Root — hồ sơ ứng viên.
 * - Tham chiếu Job/Application bằng ID (ko thông qua object)
 * - ContactInfo: @Embedded (nhúng vào bảng candidates).
 * - CvFile: Entity con (sub-entity) — cascade theo Candidate.
 * - ParsedCvData: VO lưu dạng JSON (jsonb trên Postgres).
 */
@Getter
@Entity
@Table(name = "candidates")
@SQLRestriction("is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Candidate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "full_name", nullable = false, length = 200)
    private String fullName;

    @Embedded
    private ContactInfo contact;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "cv_file_id")
    private CvFile cv;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "parsed_cv_data", columnDefinition = "jsonb")
    private ParsedCvData parsed;

    public static Candidate register(String fullName, ContactInfo contact) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("fullName required");
        }
        if (contact == null) {
            throw new IllegalArgumentException("contact required");
        }
        Candidate c = new Candidate();
        c.fullName = fullName.trim();
        c.contact = contact;
        return c;
    }

    public void updateContact(ContactInfo newContact) {
        if (newContact == null) {
            throw new IllegalArgumentException("contact required");
        }
        this.contact = newContact;
    }

    public void attachCv(String fileUrl, long sizeBytes) {
        if (this.cv != null) {
            throw new IllegalStateException("CV đã được upload trước đó");
        }
        this.cv = CvFile.create(this.id, fileUrl, sizeBytes);
    }

    public void completeParsing(ParsedCvData data) {
        if (this.cv == null) {
            throw new IllegalStateException("Phải upload CV trước khi parse");
        }
        if (data == null) {
            throw new IllegalArgumentException("parsed data required");
        }
        this.cv.markParsed();
        this.parsed = data;
    }

    public void failParsing() {
        if (this.cv == null) {
            throw new IllegalStateException("CV chưa upload");
        }
        this.cv.markFailed();
    }
}

/*

ContactInfo contact = new ContactInfo("a@b.com", "0901234567", "HCM");
Candidate c = Candidate.register("Nguyen Van A", contact);
c.attachCv("s3://bucket/cv.pdf", 2_500_000);
c.completeParsing(new ParsedCvData(List.of("Java"), educations, experiences, 3));
candidateRepository.save(c);
 */
