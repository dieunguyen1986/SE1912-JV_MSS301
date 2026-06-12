package com.talenthub.job.api.controller;

public final class ApiPath {
    public static final String BASE = "/api/v1";
    public static final String JOBS = BASE + "/jobs";
    public static final String PUBLIC_JOBS = BASE + "/public/jobs";

    public static final String BY_ID = "/{id}";
    public static final String SUBMIT = "/{id}/submit";
    public static final String APPROVE = "/{id}/approve";
    public static final String PUBLISH = "/{id}/publish";
    public static final String CLOSE = "/{id}/close";

    private ApiPath() {
    }
}
