package com.talenthub.application.application.usecase;

import com.talenthub.application.domain.exception.ApplicationNotFoundException;
import com.talenthub.application.domain.model.Application;
import com.talenthub.application.domain.model.PipelineStage;
import com.talenthub.application.domain.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdvanceStageUseCase {

    private final ApplicationRepository repo;

    @Transactional
    public void execute(UUID applicationId, PipelineStage next) {
        Application app = repo.findById(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException(applicationId));
        app.advanceStage(next);   // state machine validate bên trong aggregate
        repo.save(app);
    }
}