package com.talenthub.application.infrastructure.persistence;

import com.talenthub.application.domain.model.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;


public interface OutboxEventJpaRepository extends JpaRepository<OutboxEvent, UUID> {

    @Query("SELECT o FROM OutboxEvent o WHERE o.processed=:status ORDER BY  o.createdAt ASC LIMIT 30")
    List<OutboxEvent> findUnPublished(boolean status);


}
