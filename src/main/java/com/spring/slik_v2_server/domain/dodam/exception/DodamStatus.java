package com.spring.slik_v2_server.domain.dodam.exception;

import com.spring.slik_v2_server.global.exception.statuscode.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum DodamStatus implements StatusCode {

	ALREADY_CHECKED_STUDENT(HttpStatus.CONFLICT, "ALREADY_CHECKED_STUDENT", "이미 심자 명단에 있는 학생입니다.");

	private HttpStatus httpStatus;
	private String code;
	private String message;
}
