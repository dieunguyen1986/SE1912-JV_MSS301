package com.talenthub.job.infrastructure;

import com.talenthub.job.domain.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface JobJpaRepository extends JpaRepository<Job, UUID> {

    @Query("""
            SELECT j FROM Job j
            WHERE (:keyword IS NULL OR :keyword = ''
                   OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<Job> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
            SELECT j FROM Job j
            WHERE j.status = com.talenthub.job.domain.Job.Status.PUBLISHED
              AND (:keyword IS NULL OR :keyword = ''
                   OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<Job> searchPublished(@Param("keyword") String keyword, Pageable pageable);

    boolean existsByTitle(String title);
}
