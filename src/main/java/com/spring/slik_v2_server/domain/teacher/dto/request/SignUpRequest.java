package com.spring.slik_v2_server.domain.teacher.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(
        @NotBlank(message = "id는 필수입력입니다.")
        String id,
        @NotBlank(message = "비밀번호는 필수입력입니다.")
        String password,
        @NotBlank(message = "이름은 필수입력입니다.")
        String name,
        @NotBlank(message = "이메일은 필수입력입니다.")
        @Email
        String email,
        @NotBlank(message = "전화번호는 필수입력입니다.")
        String phone
) {
}
