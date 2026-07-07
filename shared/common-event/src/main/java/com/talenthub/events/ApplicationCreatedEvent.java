package com.talenthub.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.UUID;


public record ApplicationCreatedEvent(
        UUID eventId,

        // ID của đơn ứng tuyển vừa được tạo
        UUID applicationId,

        // ID của candidate đã nộp đơn
        UUID candidateId,

        // ID của job posting mà candidate ứng tuyển
        UUID jobId,
        String candidateEmail,
        String candidateFullName,
        String jobTitle,

        // Thời điểm event được tạo (UTC)
        Instant occurredAt
) {
    @JsonCreator
    public ApplicationCreatedEvent(@JsonProperty("eventId") UUID eventId,
                                   @JsonProperty("applicationId") UUID applicationId,
                                   @JsonProperty("candidateId") UUID candidateId,
                                   @JsonProperty("jobId") UUID jobId,
                                   @JsonProperty("candidateEmail") String candidateEmail,
                                   @JsonProperty("candidateFullName") String candidateFullName,
                                   @JsonProperty("jobTitle") String jobTitle,
                                   @JsonProperty("occurredAt") Instant occurredAt) {
        this.eventId = eventId;
        this.applicationId = applicationId;
        this.candidateId = candidateId;
        this.jobId = jobId;
        this.candidateEmail = candidateEmail;
        this.candidateFullName = candidateFullName;
        this.jobTitle = jobTitle;
        this.occurredAt = occurredAt;
    }
}
