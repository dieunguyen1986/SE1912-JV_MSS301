package com.talenthub.application.application.usecase;

import com.talenthub.application.domain.exception.ApplicationNotFoundException;
import com.talenthub.application.domain.model.Application;
import com.talenthub.application.domain.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/** UC-02.18 — Update Offer Status: ứng viên ACCEPT → HIRED, hoặc DECLINE → OFFER_DECLINED. */
@Service
@RequiredArgsConstructor
public class DecideOfferUseCase {

    public enum Decision {ACCEPT, DECLINE}

    private final ApplicationRepository repo;

    @Transactional
    public Application execute(UUID applicationId, Decision decision) {
        Application app = repo.findById(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException(applicationId));
        switch (decision) {
            case ACCEPT -> app.acceptOffer();
            case DECLINE -> app.declineOffer();
        }
        return repo.save(app);
    }
}
