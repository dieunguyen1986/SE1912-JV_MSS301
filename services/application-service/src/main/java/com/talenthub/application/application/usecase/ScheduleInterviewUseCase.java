package com.talenthub.application.application.usecase;

import com.talenthub.application.application.command.ScheduleInterviewCommand;
import com.talenthub.application.domain.exception.ApplicationNotFoundException;
import com.talenthub.application.domain.model.Application;
import com.talenthub.application.domain.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** UC-02.11 / UC-02.15 — Schedule Technical / HM Interview. */
@Service
@RequiredArgsConstructor
public class ScheduleInterviewUseCase {

    private final ApplicationRepository repo;

    @Transactional
    public Application execute(ScheduleInterviewCommand cmd) {
        Application app = repo.findById(cmd.applicationId())
                .orElseThrow(() -> new ApplicationNotFoundException(cmd.applicationId()));
        app.scheduleInterview(cmd.scheduledAt(), cmd.interviewerName());
        return repo.save(app);
    }
}
