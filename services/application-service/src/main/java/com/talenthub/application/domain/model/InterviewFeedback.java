package com.talenthub.application.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Value Object: kết quả chấm 1 buổi phỏng vấn (embedded trong {@link Interview}).
 * Bất biến + validate score 1..10 ngay tại constructor.
 */
@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewFeedback {

    @Column(name = "feedback_score")
    private int score;

    @Column(name = "feedback_comment", length = 2000)
    private String comment;

    @Column(name = "feedback_interviewer")
    private String interviewerName;

    public InterviewFeedback(int score, String comment, String interviewerName) {
        if (score < 1 || score > 10) {
            throw new IllegalArgumentException("Score phải trong khoảng 1-10");
        }
        this.score = score;
        this.comment = comment;
        this.interviewerName = interviewerName;
    }
}
