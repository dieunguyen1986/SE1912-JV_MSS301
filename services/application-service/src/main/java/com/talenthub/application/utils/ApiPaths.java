package com.talenthub.application.utils;

public final class ApiPaths {
    public static final String BASE = "/api/v1";
    public static final String APPLICATIONS = BASE + "/applications";

    public static final String BY_ID = "/{id}";
    public static final String STAGE = "/{id}/stage";
    public static final String NOTES = "/{id}/notes";
    public static final String INTERVIEW = "/{id}/interview";
    public static final String INTERVIEW_RESULT = "/{id}/interview/result";
    public static final String OFFER = "/{id}/offer";
    public static final String OFFER_DECISION = "/{id}/offer/decision";

    private ApiPaths() {
    }
}
