package com.spring.slik_v2_server.domain.teacher.dto.request;

public record ChangePasswordRequest(
        String current_password,
        String new_password
) {
}
