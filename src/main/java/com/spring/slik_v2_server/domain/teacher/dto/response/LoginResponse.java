package com.spring.slik_v2_server.domain.teacher.dto.response;

import com.spring.slik_v2_server.domain.teacher.entity.Teacher;

public record LoginResponse(
        String accessToken,
        int expiresIn,
        UserResponse user
) {
    public static LoginResponse of(String accessToken, int expiresIn, Teacher teacher) {
        return new LoginResponse(
                accessToken,
                expiresIn,
                UserResponse.of(teacher)
        );
    }
}
