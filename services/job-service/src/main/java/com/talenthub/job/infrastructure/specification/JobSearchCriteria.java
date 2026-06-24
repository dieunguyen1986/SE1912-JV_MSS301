package com.talenthub.job.infrastructure.specification;

import com.talenthub.job.domain.aggregate.JobAggregate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobSearchCriteria {
    private String keyword;
    private JobAggregate.Status status;
    private UUID departmentId;
    private LocalDate deadlineFrom;
    private LocalDate deadlineTo;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
}
