package com.talenthub.job.application.command;

import com.talenthub.job.domain.Job;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor@AllArgsConstructor
@Setter@Getter
public class DraftJobCommand {
    private String title;
    private String description;
    private String location;
    private UUID departmentId;
    private Set<String> requiredSkills = new HashSet<>();
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private LocalDate deadline;
}
