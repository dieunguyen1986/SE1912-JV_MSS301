package com.talenthub.application.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * Entity con của Application: 1 dòng note đánh giá do Recruiter/HM ghi tại 1 stage.
 * Truy cập qua aggregate root {@link Application#addNote(String, String)}.
 */
@Getter
@Entity
@Table(name = "evaluation_notes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EvaluationNote {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String author;

    @Column(nullable = false, length = 2000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "stage", nullable = false, length = 30)
    private PipelineStage stage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    static EvaluationNote create(String author, String content, PipelineStage stage) {
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("author must not be blank");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("content must not be blank");
        }
        EvaluationNote note = new EvaluationNote();
        note.id = UUID.randomUUID();
        note.author = author.trim();
        note.content = content.trim();
        note.stage = stage;
        note.createdAt = Instant.now();
        return note;
    }
}
