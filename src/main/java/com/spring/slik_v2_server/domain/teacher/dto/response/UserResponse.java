package com.spring.slik_v2_server.domain.teacher.dto.response;

import com.spring.slik_v2_server.domain.teacher.entity.Role;
import com.spring.slik_v2_server.domain.teacher.entity.Teacher;

public record UserResponse(
        String id,
        String name,
        Role role
) {
    public static UserResponse of(Teacher teacher) {
        return new UserResponse(
                teacher.getUsername(),
                teacher.getName(),
                teacher.getRole()
        );
    }
}
