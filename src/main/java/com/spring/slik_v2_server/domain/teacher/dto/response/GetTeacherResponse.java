package com.spring.slik_v2_server.domain.teacher.dto.response;

import com.spring.slik_v2_server.domain.teacher.entity.Role;
import com.spring.slik_v2_server.domain.teacher.entity.Teacher;

public record GetTeacherResponse(
        String id,
        String name,
        boolean isActive,
        Role role
) {
    public static GetTeacherResponse of(Teacher teacher) {
        return new GetTeacherResponse(
                teacher.getUsername(),
                teacher.getName(),
                teacher.isActive(),
                teacher.getRole()
        );
    }
}
