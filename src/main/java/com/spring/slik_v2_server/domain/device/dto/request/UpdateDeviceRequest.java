package com.spring.slik_v2_server.domain.device.dto.request;

import com.spring.slik_v2_server.domain.attendance.entity.AttendanceType;

public record UpdateDeviceRequest(
        String student_id,
        AttendanceType new_status
) {
}
