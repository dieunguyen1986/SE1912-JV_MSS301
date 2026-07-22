package com.talenthub.candidate.application.command;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record AttachCvCommand(UUID candidateId, MultipartFile file) {
}
