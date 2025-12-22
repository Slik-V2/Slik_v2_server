package com.spring.slik_v2_server.domain.fingerprint.service;

import com.spring.slik_v2_server.domain.fingerprint.dto.request.FingerPrintRequest;
import com.spring.slik_v2_server.domain.fingerprint.entity.FingerPrint;
import com.spring.slik_v2_server.domain.fingerprint.exception.FingerPrintErrorCode;
import com.spring.slik_v2_server.domain.fingerprint.exception.FingerPrintException;
import com.spring.slik_v2_server.domain.fingerprint.repository.FingerPrintRepository;
import com.spring.slik_v2_server.global.data.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FingerPrintService {

	private final FingerPrintRepository fingerPrintRepository;

	// 지문 등록
	public ApiResponse<HttpStatus> create(FingerPrintRequest request) {
		if (!request.studentId().equals("0000") && fingerPrintRepository.existsByStudentId(request.studentId())) {
			throw new FingerPrintException(FingerPrintErrorCode.STUDENT_ID_ALREADY_EXISTS);
		}

		FingerPrint fingerPrint = FingerPrint.builder()
				.studentId(request.studentId())
				.name(request.name())
				.encrypted_template(request.encrypted_template())
				.build();

		fingerPrintRepository.save(fingerPrint);
		return ApiResponse.ok(HttpStatus.CREATED);
	}
}
