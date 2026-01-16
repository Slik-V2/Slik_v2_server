package com.spring.slik_v2_server.domain.student.exception;

import com.spring.slik_v2_server.global.exception.statuscode.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StudentStatus implements StatusCode {
    STUDENT_NOT_FOUND(HttpStatus.NOT_FOUND, "STUDENT_NOT_FOUND", "학생 정보를 찾을 수 없습니다.");
    private HttpStatus httpStatus;
    private String code;
    private String message;
}
