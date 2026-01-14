package com.spring.slik_v2_server.domain.fingerprint.exception;

import com.spring.slik_v2_server.global.exception.statuscode.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FingerPrintStatusCode implements StatusCode {

	INVALID_API_KEY(HttpStatus.UNAUTHORIZED, "INVALID_API_KEY", "API 키가 맞지 않습니다"),
	STUDENT_ID_ALREADY_EXISTS(HttpStatus.CONFLICT, "STUDENT_ID_ALREADY_EXISTS", "이미 존재하는 학번입니다."),
	STUDENT_NOT_FOUND(HttpStatus.NOT_FOUND, "STUDENT_NOT_FOUND", "학생을 찾을 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}
