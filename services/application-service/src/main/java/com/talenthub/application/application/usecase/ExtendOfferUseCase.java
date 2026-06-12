package com.talenthub.application.application.usecase;

import com.talenthub.application.application.command.ExtendOfferCommand;
import com.talenthub.application.domain.exception.ApplicationNotFoundException;
import com.talenthub.application.domain.model.Application;
import com.talenthub.application.domain.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Gửi offer cho ứng viên đã qua vòng HM → chuyển stage sang OFFERED. */
@Service
@RequiredArgsConstructor
public class ExtendOfferUseCase {

    private final ApplicationRepository repo;

    @Transactional
    public Application execute(ExtendOfferCommand cmd) {
        Application app = repo.findById(cmd.applicationId())
                .orElseThrow(() -> new ApplicationNotFoundException(cmd.applicationId()));
        app.extendOffer(cmd.salary(), cmd.startDate());
        return repo.save(app);
    }
}
