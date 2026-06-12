package com.talenthub.application.infrastructure.persistence;

import com.talenthub.application.domain.model.Application;
import com.talenthub.application.domain.model.PipelineStage;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Dynamic query cho Kanban board (lọc theo jobId / stage). */
final class ApplicationSpecifications {

    private ApplicationSpecifications() {
    }

    static Specification<Application> byJobAndStage(UUID jobId, PipelineStage stage) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (jobId != null) {
                predicates.add(cb.equal(root.get("jobId"), jobId));
            }
            if (stage != null) {
                predicates.add(cb.equal(root.get("currentStage"), stage));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
