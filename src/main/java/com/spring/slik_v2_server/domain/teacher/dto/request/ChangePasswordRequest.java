package com.spring.slik_v2_server.domain.teacher.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(
        @NotBlank(message = "이전 비번은 필수 입력입니다.")
        String currentPassword,
        @NotBlank(message = "새 비번은 필수 입력입니다.")
        String newPassword
) {
}
