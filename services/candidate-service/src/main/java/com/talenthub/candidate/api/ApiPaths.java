package com.talenthub.candidate.api;

public final class ApiPaths {
    public static final String BASE = "/api/v1";
    public static final String CANDIDATES = BASE + "/candidates";
    public static final String CV = CANDIDATES + "/{id}/cv";
    private ApiPaths() {}
}
