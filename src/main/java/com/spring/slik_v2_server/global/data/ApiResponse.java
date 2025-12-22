package com.spring.slik_v2_server.global.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.slik_v2_server.global.exception.statuscode.StatusCode;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
		int status,
		T data,
		ErrorResponse error
) {

	public static <T> ApiResponse<T> ok(T data) {
		return new ApiResponse<>(HttpStatus.OK.value(), data, null);
	}

	public static ApiResponse<Void> error(HttpStatus status, ErrorResponse error) {
		return new ApiResponse<>(status.value(), null, error);
	}

	public static <T> ApiResponse<T> error(StatusCode status) {
		return new ApiResponse<>(status.getHttpStatus().value(), null, ErrorResponse.of(status.getCode(), status.getMessage()));
	}
}
