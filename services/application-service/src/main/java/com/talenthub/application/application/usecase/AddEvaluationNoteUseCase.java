package com.talenthub.application.application.usecase;

import com.talenthub.application.application.command.AddNoteCommand;
import com.talenthub.application.domain.exception.ApplicationNotFoundException;
import com.talenthub.application.domain.model.Application;
import com.talenthub.application.domain.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** UC-02.8 — Add Evaluation Notes (gắn vào stage hiện tại). */
@Service
@RequiredArgsConstructor
public class AddEvaluationNoteUseCase {

    private final ApplicationRepository repo;

    @Transactional
    public Application execute(AddNoteCommand cmd) {
        Application app = repo.findById(cmd.applicationId())
                .orElseThrow(() -> new ApplicationNotFoundException(cmd.applicationId()));
        app.addNote(cmd.author(), cmd.content());
        return repo.save(app);
    }
}
