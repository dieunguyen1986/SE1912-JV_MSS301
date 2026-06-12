package com.talenthub.application.application.usecase;

import com.talenthub.application.application.command.CompleteInterviewCommand;
import com.talenthub.application.domain.exception.ApplicationNotFoundException;
import com.talenthub.application.domain.model.Application;
import com.talenthub.application.domain.model.InterviewFeedback;
import com.talenthub.application.domain.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** UC-02.12 — Import / record Interview Results. */
@Service
@RequiredArgsConstructor
public class CompleteInterviewUseCase {

    private final ApplicationRepository repo;

    @Transactional
    public Application execute(CompleteInterviewCommand cmd) {
        Application app = repo.findById(cmd.applicationId())
                .orElseThrow(() -> new ApplicationNotFoundException(cmd.applicationId()));
        app.completeInterview(new InterviewFeedback(cmd.score(), cmd.comment(), cmd.interviewerName()));
        return repo.save(app);
    }
}
