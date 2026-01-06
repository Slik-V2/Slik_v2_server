package com.spring.slik_v2_server.domain.attendance.dto.response;

import com.spring.slik_v2_server.domain.attendance.entity.AttendanceStatus;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceType;
import com.spring.slik_v2_server.domain.attendance.entity.NightStudyStatus;

import java.time.LocalDateTime;
import java.util.List;

public record AttendanceStatusResponse(
        String student_id,
        String name,
        NightStudyStatus status,
        LocalDateTime nightStudy1LastAction,
        LocalDateTime nightStudy2LastAction
) {
    public static AttendanceStatusResponse of(AttendanceStatus attendanceStatus) {
        return new AttendanceStatusResponse(
                attendanceStatus.getFingerPrint().getStudentId(),
                attendanceStatus.getFingerPrint().getName(),
                attendanceStatus.getStatus(),
                attendanceStatus.getNightStudy1LastAction(),
                attendanceStatus.getNightStudy2LastAction()
        );
    }

    public static List<AttendanceStatusResponse> from(List<AttendanceStatus> attendanceStatuses) {
        return attendanceStatuses
                .stream()
                .map(AttendanceStatusResponse::of)
                .toList();
    }
}
