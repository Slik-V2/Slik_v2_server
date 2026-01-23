package com.spring.slik_v2_server.domain.device.dto.request;

import com.spring.slik_v2_server.domain.attendance.entity.AttendanceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateDeviceRequest(
        @NotBlank(message = "학번을 입력해주세요.")
        String studentId,
        @NotNull(message = "세션을 입력해주세요.")
        String targetSession,
        @NotNull(message = "상태를 입력해주세요.")
        AttendanceStatus newStatus
) {
}
