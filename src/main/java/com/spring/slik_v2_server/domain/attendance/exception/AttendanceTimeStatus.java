package com.spring.slik_v2_server.domain.attendance.exception;

import com.spring.slik_v2_server.global.exception.statuscode.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AttendanceTimeStatus implements StatusCode {

    NOT_IN_ATTENDANCE_TIME(HttpStatus.BAD_REQUEST, "ATTENDANCE_TIME_INVALID", "출석 가능 시간이 아닙니다."),
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "DATA_NOT_FOUND", "조회할 데이터를 찾을 수 없습니다."),
    DATE_NOT_FOUND(HttpStatus.NOT_FOUND, "DATE_NOT_FOUND", "해당 날짜의 자습 시간을 조회할 수 없습니다."),
    TIME_OUT_OF_RANGE(HttpStatus.BAD_REQUEST, "TIME_OUT_OF_RANGE", "시간 설정 범위가 잘못되었습니다. 다시 시도해주세요.");

    private HttpStatus httpStatus;
    private String code;
    private String message;
}
