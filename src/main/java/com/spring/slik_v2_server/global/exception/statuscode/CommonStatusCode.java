package com.spring.slik_v2_server.global.exception.statuscode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonStatusCode implements StatusCode {

	INVALID_ARGUMENT("INVALID_ARGUMENT", "잘못된 인자입니다.", HttpStatus.BAD_REQUEST),
	UNKNOWN_ENDPOINT("UNKNOWN_ENDPOINT", "알 수 없는 엔드포인트 입니다.", HttpStatus.NOT_FOUND),
	INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "서버에 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

	private final String code;
	private final String message;
	private final HttpStatus httpStatus;
}
