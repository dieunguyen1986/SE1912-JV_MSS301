package com.talenthub.candidate.infrastructure.persistence;

import com.talenthub.candidate.domain.model.CvFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CvFileJpaRepository extends JpaRepository<CvFile, UUID> {
}
