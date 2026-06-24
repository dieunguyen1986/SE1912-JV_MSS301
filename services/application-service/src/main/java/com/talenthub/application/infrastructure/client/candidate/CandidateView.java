package com.talenthub.application.infrastructure.client.candidate;

import java.util.UUID;

/**
 * Projection của candidate-service. Các field thừa trong response (cv, parsed, timestamps...)
 * sẽ bị Jackson bỏ qua (FAIL_ON_UNKNOWN_PROPERTIES = false mặc định trong Spring Boot).
 */
public record CandidateView(
        UUID id,
        String fullName,
        Contact contact
) {
    public record Contact(String email, String phone, String address) {}

    public String email() {
        return contact == null ? null : contact.email();
    }
}
