package com.spring.slik_v2_server.domain.teacher.exception;

import com.spring.slik_v2_server.global.exception.statuscode.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TeacherStatusCode implements StatusCode {
    TEACHER_NOT_FOUND(HttpStatus.NOT_FOUND, "TEACHER_NOT_FOUND", "선생님을 찾을 수 없습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED, "PASSWORD_NOT_MATCH", "비밀번호가 일치하지 않습니다."),
    SAME_AS_OLD_PASSWORD(HttpStatus.BAD_REQUEST, "SAME_AS_OLD_PASSWORD", "기존 비밀번호와 새로운 비밀번호가 같습니다."),
    ADMIN_ACCOUNT_CANNOT_BE_MODIFIED(HttpStatus.FORBIDDEN, "ADMIN_ACCOUNT_CANNOT_BE_MODIFIED", "admin 계정은 변경할 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
