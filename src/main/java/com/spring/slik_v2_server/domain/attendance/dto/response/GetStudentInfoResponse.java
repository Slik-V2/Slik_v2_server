package com.spring.slik_v2_server.domain.attendance.dto.response;

import com.spring.slik_v2_server.domain.student.entity.Student;

public record GetStudentInfoResponse(
        String StudentId,
        String name,
        StatusResponse stats
) {
    public static GetStudentInfoResponse of(Student student) {
        return new GetStudentInfoResponse(
                student.getStudentId(),
                student.getName(),
                StatusResponse.of(student)
        );
    }
}
