package com.spring.slik_v2_server.domain.fingerprint.dto.request;

public record FingerPrintRequest(
		String studentId,
		String name,
		String encrypted_template
) {
}
