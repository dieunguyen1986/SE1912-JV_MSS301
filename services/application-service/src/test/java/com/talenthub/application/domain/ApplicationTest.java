package com.talenthub.application.domain;

import com.talenthub.application.domain.model.Application;
import com.talenthub.application.domain.model.InterviewFeedback;
import com.talenthub.application.domain.model.PipelineStage;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Unit test thuần domain — pipeline state machine (BRULE-12), interview, offer, notes. */
class ApplicationTest {

    private Application submitted() {
        return Application.submit(UUID.randomUUID(), UUID.randomUUID());
    }

    /** Đưa application qua tới đúng stage INTERVIEW_HM. */
    private Application atInterviewHm() {
        Application app = submitted();
        app.advanceStage(PipelineStage.CV_SCREENING);
        app.advanceStage(PipelineStage.INTERVIEW_TECHNICAL);
        app.advanceStage(PipelineStage.INTERVIEW_HM);
        return app;
    }

    @Test
    void submit_startsAtNew() {
        Application app = submitted();
        assertThat(app.getCurrentStage()).isEqualTo(PipelineStage.NEW);
        assertThat(app.getSubmittedAt()).isNotNull();
    }

    @Test
    void submit_requiresIds() {
        assertThatThrownBy(() -> Application.submit(null, UUID.randomUUID()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void advance_followsBRULE12() {
        Application app = submitted();
        app.advanceStage(PipelineStage.CV_SCREENING);
        assertThat(app.getCurrentStage()).isEqualTo(PipelineStage.CV_SCREENING);
        app.advanceStage(PipelineStage.INTERVIEW_TECHNICAL);
        assertThat(app.getCurrentStage()).isEqualTo(PipelineStage.INTERVIEW_TECHNICAL);
    }

    @Test
    void advance_cannotSkipStages_BRULE12() {
        Application app = submitted();
        assertThatThrownBy(() -> app.advanceStage(PipelineStage.OFFERED))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void advance_cannotGoBackward_BRULE12() {
        Application app = submitted();
        app.advanceStage(PipelineStage.CV_SCREENING);
        assertThatThrownBy(() -> app.advanceStage(PipelineStage.NEW))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void scheduleAndCompleteInterview() {
        Application app = submitted();
        app.advanceStage(PipelineStage.CV_SCREENING);
        app.scheduleInterview(LocalDateTime.now().plusDays(2), "Alice");
        assertThat(app.getInterview()).isNotNull();
        app.completeInterview(new InterviewFeedback(8, "Strong", "Alice"));
        assertThat(app.getInterview().getFeedback().getScore()).isEqualTo(8);
    }

    @Test
    void scheduleInterview_rejectedAtWrongStage() {
        Application app = submitted(); // NEW
        assertThatThrownBy(() -> app.scheduleInterview(LocalDateTime.now().plusDays(1), "Bob"))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void extendOffer_requiresInterviewHm() {
        Application app = submitted();
        assertThatThrownBy(() -> app.extendOffer(new BigDecimal("2000"), LocalDate.now().plusDays(30)))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void fullHappyPath_toHired() {
        Application app = atInterviewHm();
        app.extendOffer(new BigDecimal("2500"), LocalDate.now().plusDays(30));
        assertThat(app.getCurrentStage()).isEqualTo(PipelineStage.OFFERED);
        app.acceptOffer();
        assertThat(app.getCurrentStage()).isEqualTo(PipelineStage.HIRED);
    }

    @Test
    void declineOffer_movesToDeclined() {
        Application app = atInterviewHm();
        app.extendOffer(new BigDecimal("2500"), LocalDate.now().plusDays(30));
        app.declineOffer();
        assertThat(app.getCurrentStage()).isEqualTo(PipelineStage.OFFER_DECLINED);
    }

    @Test
    void addNote_recordsCurrentStage() {
        Application app = submitted();
        app.advanceStage(PipelineStage.CV_SCREENING);
        app.addNote("recruiter1", "Good fit");
        assertThat(app.getNotes()).hasSize(1);
        assertThat(app.getNotes().get(0).getStage()).isEqualTo(PipelineStage.CV_SCREENING);
    }

    @Test
    void interviewFeedback_validatesScoreRange() {
        assertThatThrownBy(() -> new InterviewFeedback(11, "x", "y"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
