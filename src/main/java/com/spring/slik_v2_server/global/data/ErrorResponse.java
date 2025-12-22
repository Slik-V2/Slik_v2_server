package com.spring.slik_v2_server.global.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
		String code,
		String message,
		LocalDateTime timestamp,
		Map<String, String> details
) {

	public static ErrorResponse of(String code, String message) {
		return ErrorResponse.builder()
				.code(code)
				.message(message)
				.timestamp(LocalDateTime.now())
				.build();
	}

	public static ErrorResponse of(String code, String message, Map<String, String> details) {
		return ErrorResponse.builder()
				.code(code)
				.message(message)
				.timestamp(LocalDateTime.now())
				.details(details)
				.build();
	}
}
