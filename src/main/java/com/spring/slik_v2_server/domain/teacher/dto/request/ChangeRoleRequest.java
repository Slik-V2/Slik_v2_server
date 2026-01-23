package com.spring.slik_v2_server.domain.teacher.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChangeRoleRequest(
        @NotBlank(message = "role값을 입력해주세요.")
        String role
) {
}
