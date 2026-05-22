package com.talenthub.candidate.application.command;

public record RegisterCandidateCommand(String fullName, String email, String phone, String address) {
}
