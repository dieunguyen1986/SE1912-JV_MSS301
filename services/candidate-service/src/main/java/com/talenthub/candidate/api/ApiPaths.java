package com.talenthub.candidate.api;

public final class ApiPaths {
    public static final String BASE = "/api/v1";
    public static final String CANDIDATES = BASE + "/candidates";

    public static final String BY_ID = "/{id}";
    public static final String BY_EMAIL = "/by-email";
    public static final String CONTACT = "/{id}/contact";
    public static final String CV = "/{id}/cv";
    public static final String CV_PARSED = "/{id}/cv/parsed";
    public static final String CV_PARSE_FAILED = "/{id}/cv/parse-failed";

    private ApiPaths() {
    }
}
