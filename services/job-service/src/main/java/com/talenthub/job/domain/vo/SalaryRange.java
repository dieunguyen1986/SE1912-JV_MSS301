package com.talenthub.job.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalaryRange {

    @Column(name = "min_salary", nullable = false, precision = 15, scale = 2)
    private BigDecimal min;

    @Column(name = "max_salary", nullable = false, precision = 15, scale = 2)
    private BigDecimal max;

    public SalaryRange(BigDecimal min, BigDecimal max) {
        if (min == null || max == null) {
            throw new IllegalArgumentException("min/max salary must not be null");
        }
        if (min.signum() <= 0 || max.signum() <= 0) {
            throw new IllegalArgumentException("Salary must be > 0");
        }
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("min must be <= max");
        }
        this.min = min;
        this.max = max;
    }

    public boolean contains(BigDecimal salary) {
        return salary.compareTo(min) >= 0 && salary.compareTo(max) <= 0;
    }
}
