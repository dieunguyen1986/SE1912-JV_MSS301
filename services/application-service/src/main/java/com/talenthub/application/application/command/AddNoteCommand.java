package com.talenthub.application.application.command;

import java.util.UUID;

public record AddNoteCommand(UUID applicationId, String author, String content) {
}
