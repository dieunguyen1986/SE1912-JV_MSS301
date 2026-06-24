package com.talenthub.job.application.command;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter@Setter
@NoArgsConstructor@AllArgsConstructor
@ToString
public class JobCommand {

    private String title;
    private String description;
    private UUID departmentId;
    private Set<String> requiredSkills = new HashSet<>();
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private LocalDate deadline;
}
