package com.talenthub.job.infrastructure.specification;

import com.talenthub.job.infrastructure.persistence.entity.Job;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class JobSpecifications {

    private JobSpecifications() {
    }

    public static Specification<Job> fromCriteria(JobSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getKeyword() != null && !criteria.getKeyword().isBlank()) {
                String like = "%" + criteria.getKeyword().toLowerCase().trim() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), like),
                        cb.like(cb.lower(root.get("description")), like)
                ));
            }
            if (criteria.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"),
                        Job.Status.valueOf(criteria.getStatus().name())));
            }
            if (criteria.getDepartmentId() != null) {
                predicates.add(cb.equal(root.get("departmentId"), criteria.getDepartmentId()));
            }
            if (criteria.getDeadlineFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("deadline"), criteria.getDeadlineFrom()));
            }
            if (criteria.getDeadlineTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("deadline"), criteria.getDeadlineTo()));
            }
            if (criteria.getMinSalary() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("salaryRange").get("min"), criteria.getMinSalary()));
            }
            if (criteria.getMaxSalary() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("salaryRange").get("max"), criteria.getMaxSalary()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
