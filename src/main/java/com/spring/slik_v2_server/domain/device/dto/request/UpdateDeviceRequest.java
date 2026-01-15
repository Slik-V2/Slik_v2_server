package com.spring.slik_v2_server.domain.device.dto.request;

import com.spring.slik_v2_server.domain.attendance.entity.AttendanceType;
import jakarta.validation.constraints.NotBlank;

public record UpdateDeviceRequest(
        @NotBlank(message = "학번을 입력해주세요.")
        String student_id,
        AttendanceType new_status
) {
}
