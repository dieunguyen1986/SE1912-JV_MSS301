package com.talenthub.notification.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationLog {

    public enum Status { PENDING, SENT, FAILED }

    private UUID id;
    private String recipientEmail;
    private String templateCode;
    private Status status;
    private int attemptCount;
    private Instant createdAt;
    private Instant lastAttemptAt;
    private String errorMessage;

    public static NotificationLog queue(String recipientEmail, String templateCode) {
        if (recipientEmail == null || !recipientEmail.contains("@")) {
            throw new IllegalArgumentException("Invalid recipientEmail");
        }
        NotificationLog log = new NotificationLog();
        log.id = UUID.randomUUID();
        log.recipientEmail = recipientEmail;
        log.templateCode = templateCode;
        log.status = Status.PENDING;
        log.attemptCount = 0;
        log.createdAt = Instant.now();
        return log;
    }

    public void markSent() {
        this.status = Status.SENT;
        this.lastAttemptAt = Instant.now();
        this.attemptCount++;
    }

    public void markFailed(String errorMessage) {
        this.attemptCount++;
        this.lastAttemptAt = Instant.now();
        this.errorMessage = errorMessage;
        this.status = attemptCount >= 3 ? Status.FAILED : Status.PENDING;
    }

    public boolean canRetry() {
        return status == Status.PENDING && attemptCount < 3;
    }
}