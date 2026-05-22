package com.talenthub.job.infrastructure;

import com.talenthub.job.domain.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface JobJpaRepository extends JpaRepository<Job, UUID> {

    @Query("SELECT j FROM Job j WHERE LOWER(j.title) LIKE LOWER(:keyword) OR LOWER(j.description) LIKE LOWER(:keyword)")
    Page<Job> getJobs(Pageable pageable, String keyword);

    // Method Creation
    boolean existsByTitle(String title);

}
