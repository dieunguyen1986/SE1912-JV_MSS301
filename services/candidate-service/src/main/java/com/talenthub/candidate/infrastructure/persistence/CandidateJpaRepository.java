package com.talenthub.candidate.infrastructure.persistence;

import com.talenthub.candidate.domain.model.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CandidateJpaRepository extends JpaRepository<Candidate, UUID> {
    boolean existsByContactEmail(String email);

    Optional<Candidate> findByContactEmail(String email);

    @Query("""
            SELECT c FROM Candidate c
            WHERE (:keyword IS NULL OR :keyword = ''
                   OR LOWER(c.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(c.contact.email) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<Candidate> search(@Param("keyword") String keyword, Pageable pageable);
}
