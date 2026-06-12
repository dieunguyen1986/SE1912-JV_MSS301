package com.talenthub.application.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Entity con của Application: offer cuối cùng gửi cho ứng viên. Truy cập qua
 * aggregate root ({@link Application#extendOffer}, accept/decline).
 */
@Getter
@Entity
@Table(name = "offers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Offer {

    public enum Status { EXTENDED, ACCEPTED, DECLINED }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal salary;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @Column(name = "extended_at", nullable = false)
    private Instant extendedAt;

    static Offer extend(BigDecimal salary, LocalDate startDate) {
        if (salary == null || salary.signum() <= 0) {
            throw new IllegalArgumentException("salary phải > 0");
        }
        if (startDate == null || startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("startDate phải ở tương lai");
        }
        Offer o = new Offer();
        o.id = UUID.randomUUID();
        o.salary = salary;
        o.startDate = startDate;
        o.status = Status.EXTENDED;
        o.extendedAt = Instant.now();
        return o;
    }

    void accept() {
        if (status != Status.EXTENDED) {
            throw new IllegalStateException("Chỉ offer EXTENDED mới accept được");
        }
        this.status = Status.ACCEPTED;
    }

    void decline() {
        if (status != Status.EXTENDED) {
            throw new IllegalStateException("Chỉ offer EXTENDED mới decline được");
        }
        this.status = Status.DECLINED;
    }
}
