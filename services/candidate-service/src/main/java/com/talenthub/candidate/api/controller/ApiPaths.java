package com.talenthub.candidate.api.controller;

public final class ApiPaths {
    public static final String BASE = "/api/v1";
    public static final String CANDIDATES = BASE + "/candidates";
    public static final String CANDIDATE_BY_ID = "/{id}";
    public static final String CONTACT = "/{id}/contact";
    public static final String CV = "/{id}/cv";
    private ApiPaths() {}
}
