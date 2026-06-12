package com.talenthub.application.application.command;

import java.time.LocalDateTime;
import java.util.UUID;

public record ScheduleInterviewCommand(UUID applicationId, LocalDateTime scheduledAt, String interviewerName) {
}
