package com.spring.slik_v2_server.domain.fingerprint.exception;

import com.spring.slik_v2_server.global.exception.statuscode.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FingerPrintStatusCode implements StatusCode {

	STUDENT_ID_ALREADY_EXISTS(HttpStatus.CONFLICT, "STUDENT_ID_ALREADY_EXISTS", "이미 존재하는 학번입니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}
