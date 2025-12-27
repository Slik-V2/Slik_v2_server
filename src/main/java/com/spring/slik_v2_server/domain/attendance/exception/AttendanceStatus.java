package com.spring.slik_v2_server.domain.attendance.exception;

import com.spring.slik_v2_server.global.exception.statuscode.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AttendanceStatus implements StatusCode {

    NOT_IN_ATTENDANCE_TIME(HttpStatus.BAD_REQUEST, "ATTENDANCE_TIME_INVALID", "출석 가능 시간이 아닙니다.");

    private HttpStatus httpStatus;
    private String code;
    private String message;
}
