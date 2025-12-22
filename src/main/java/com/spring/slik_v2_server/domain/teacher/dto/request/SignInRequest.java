package com.spring.slik_v2_server.domain.teacher.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        @NotBlank(message = "id는 필수입력입니다.")
        String id,
        @NotBlank(message = "비밀번호는 필수입력입니다.")
        String password
) {
}
