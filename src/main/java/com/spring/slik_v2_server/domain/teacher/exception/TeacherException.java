package com.spring.slik_v2_server.domain.teacher.exception;

import lombok.Getter;

@Getter
public class TeacherException extends RuntimeException {
    private final TeacherErrorCode teacherErrorCode;

    public TeacherException(TeacherErrorCode teacherErrorCode) {
        super(teacherErrorCode.getMessage());
        this.teacherErrorCode = teacherErrorCode;
    }

    public TeacherException(TeacherErrorCode teacherErrorCode, String message) {
        super(message);
        this.teacherErrorCode = teacherErrorCode;
    }
}
