package com.talenthub.application.domain.repository;

import com.talenthub.application.domain.model.OutboxEvent;


import java.util.List;

public interface OutboxEventRepository {
    OutboxEvent save(OutboxEvent outboxEvent);
    List<OutboxEvent> findUnPublished(boolean status);
}
