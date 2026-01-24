package com.spring.slik_v2_server.domain.device.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FindAttendanceRequest(
        @NotBlank(message = "학번 입력을 해주세요.")
        String studentId
) {
}
