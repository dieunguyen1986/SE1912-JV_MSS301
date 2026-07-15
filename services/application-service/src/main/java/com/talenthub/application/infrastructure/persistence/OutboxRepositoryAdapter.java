package com.talenthub.application.infrastructure.persistence;

import com.talenthub.application.domain.model.OutboxEvent;
import com.talenthub.application.domain.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
@Component
@RequiredArgsConstructor
@Qualifier("outboxEventRepository")
public class OutboxRepositoryAdapter implements OutboxEventRepository {

    private  final OutboxEventJpaRepository outboxEventJpaRepository;

    @Override
    public OutboxEvent save(OutboxEvent outboxEvent) {
        return outboxEventJpaRepository.save(outboxEvent);
    }

    @Override
    public List<OutboxEvent> findUnPublished(boolean status) {
        return outboxEventJpaRepository.findUnPublished(status);
    }
}
