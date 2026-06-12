package com.talenthub.application.application.command;

import java.util.UUID;

public record CompleteInterviewCommand(UUID applicationId, int score, String comment, String interviewerName) {
}
