package com.spring.slik_v2_server.domain.teacher.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TeacherErrorCode {
    TEACHER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "TEACHER_NOT_FOUND", "선생님을 찾을 수 없습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED.value(), "PASSWORD_NOT_MATCH", "비밀번호가 일치하지 않습니다."),
    SAME_AS_OLD_PASSWORD(HttpStatus.BAD_REQUEST.value(), "SAME_AS_OLD_PASSWORD", "기존 비밀번호와 새로운 비밀번호가 같습니다.");

    private int status;
    private String code;
    private String message;
}
