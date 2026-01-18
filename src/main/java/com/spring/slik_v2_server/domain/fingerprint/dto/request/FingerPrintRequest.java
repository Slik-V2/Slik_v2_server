package com.spring.slik_v2_server.domain.fingerprint.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FingerPrintRequest(
		@JsonProperty("student_id")
		String studentId,

		String encrypted_template
) {
}
