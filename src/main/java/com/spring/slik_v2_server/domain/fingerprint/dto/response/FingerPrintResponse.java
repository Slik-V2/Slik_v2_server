package com.spring.slik_v2_server.domain.fingerprint.dto.response;

import com.spring.slik_v2_server.domain.fingerprint.entity.FingerPrint;

import java.util.List;
import java.util.stream.Collectors;

public record FingerPrintResponse(
		String id,
		String studentId,
		String encrypted_template
) {

	public static FingerPrintResponse of(FingerPrint fingerPrint) {
		return new FingerPrintResponse(
				fingerPrint.getId(),
				fingerPrint.getStudentId(),
				fingerPrint.getEncrypted_template());
	}

	public static List<FingerPrintResponse> toList(List<FingerPrint> fingerPrint) {
		return fingerPrint.stream().map(
				FingerPrintResponse::of)
				.collect(Collectors.toList());
	}
}
