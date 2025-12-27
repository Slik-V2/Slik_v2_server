package com.spring.slik_v2_server.domain.device.exception;

import com.spring.slik_v2_server.global.exception.statuscode.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum DeviceStatusCode implements StatusCode {
    INVALID_API_KEY(HttpStatus.UNAUTHORIZED, "INVALID_API_KEY", "API 키가 맞지 않습니다");

    private HttpStatus httpStatus;
    private String code;
    private String message;
}
