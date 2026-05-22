package com.talenthub.candidate.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Value Object — thông tin liên hệ của Candidate.
 * Dùng @Embeddable để nhúng thẳng vào bảng candidates (không tạo bảng riêng).
 */
@Embeddable
public record ContactInfo(
        @Column(name = "email", nullable = false, length = 255)
        String email,

        @Column(name = "phone", length = 20)
        String phone,

        @Column(name = "address", length = 500)
        String address
) {
    public ContactInfo {
        if (email == null || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Email không hợp lệ");
        }
        if (phone != null && !phone.matches("^[+0-9\\s\\-()]{8,20}$")) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ");
        }
        email = email.trim().toLowerCase();
    }
}
