package com.spring.slik_v2_server.domain.fingerprint.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FingerPrintErrorCode {

	STUDENT_ID_ALREADY_EXISTS(HttpStatus.CONFLICT.value(), "STUDENT_ID_ALREADY_EXISTS", "이미 존재하는 학번입니다.");

	private Integer status;
	private String code;
	private String message;
}
