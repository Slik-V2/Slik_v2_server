package com.spring.slik_v2_server.global.exception.statuscode;

import org.springframework.http.HttpStatus;

public interface StatusCode {

	String getCode();
	String getMessage();
	HttpStatus getHttpStatus();
}
