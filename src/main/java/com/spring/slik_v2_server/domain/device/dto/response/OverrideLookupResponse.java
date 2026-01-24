package com.spring.slik_v2_server.domain.device.dto.response;

import com.spring.slik_v2_server.domain.attendance.entity.AttendanceStatus;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTime;

public record OverrideLookupResponse(
        String name,
        AttendanceStatus s1Status,
        AttendanceStatus s2Status
) {
    public static OverrideLookupResponse of(AttendanceTime attendanceTime) {
        String studentName = attendanceTime.getStudent() != null
                ? attendanceTime.getStudent().getName()
                : null;
        return new OverrideLookupResponse(
                studentName,
                attendanceTime.getS1Status(),
                attendanceTime.getS2Status()
        );
    }
}
