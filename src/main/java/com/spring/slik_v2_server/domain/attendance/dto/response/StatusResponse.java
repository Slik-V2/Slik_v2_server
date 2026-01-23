package com.spring.slik_v2_server.domain.attendance.dto.response;

import com.spring.slik_v2_server.domain.student.entity.Student;

public record StatusResponse(
        int totalDays,
        int attendedDays,
        String attendanceRate
) {
    public static StatusResponse of(Student student) {
        return new StatusResponse(
                student.getTotalDate(),
                student.getAttendanceDate(),
                String.format("%.1f", (student.getAttendanceDate() / (double) student.getTotalDate()) * 100)
        );
    }
}
