package com.spring.slik_v2_server.domain.attendance.dto.response;

import com.spring.slik_v2_server.domain.attendance.entity.AttendanceStatus;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceType;

import java.time.LocalDateTime;
import java.util.List;

public record AttendanceStatusResponse(
        LocalDateTime date,
        String student_id,
        String name,
        AttendanceType type
) {
    public static AttendanceStatusResponse of(AttendanceStatus attendanceStatus) {
        return new AttendanceStatusResponse(
                attendanceStatus.getDate(),
                attendanceStatus.getFingerPrint().getStudentId(),
                attendanceStatus.getFingerPrint().getName(),
                attendanceStatus.getAttendanceType()
        );
    }

    public static List<AttendanceStatusResponse> from(List<AttendanceStatus> attendanceStatuses) {
        return attendanceStatuses
                .stream()
                .map(AttendanceStatusResponse::of)
                .toList();
    }
}
