package com.talenthub.job.utils;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserUtils {
    // ["ROLE_ADMIN", "ROLE_REC"]
    public static List<GrantedAuthority> extractRole(String role) {
        if (!StringUtils.hasText(role)) {
            return Collections.emptyList();
        }

        // Strip surrounding brackets and split by comma: ["ROLE_ADMIN", "ROLE_REC"]
        String cleaned = role.trim()
                .replaceAll("^\\[", "")
                .replaceAll("]$", "");

        return Arrays.stream(cleaned.split(","))
                .map(r -> r.replace("\"", "").replace("'", "").trim())
                .filter(StringUtils::hasText)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
