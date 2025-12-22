package com.spring.slik_v2_server.domain.teacher.dto.request;

public record ChagePsswordRequest(
        String current_password,
        String new_password
) {
}
