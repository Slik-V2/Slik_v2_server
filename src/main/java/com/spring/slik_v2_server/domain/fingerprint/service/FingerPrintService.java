package com.spring.slik_v2_server.domain.fingerprint.service;

import com.spring.slik_v2_server.domain.fingerprint.dto.request.FingerPrintRequest;
import com.spring.slik_v2_server.domain.fingerprint.dto.response.FingerPrintResponse;
import com.spring.slik_v2_server.domain.fingerprint.entity.FingerPrint;
import com.spring.slik_v2_server.domain.fingerprint.exception.FingerPrintStatusCode;
import com.spring.slik_v2_server.domain.fingerprint.repository.FingerPrintRepository;
import com.spring.slik_v2_server.global.data.ApiResponse;
import com.spring.slik_v2_server.global.exception.ApplicationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FingerPrintService {

	private final FingerPrintRepository fingerPrintRepository;

	@Value("${spring.security.KEY}")
	private String secretKey;

	// 지문 등록
	public ApiResponse<HttpStatus> create(FingerPrintRequest request, HttpServletRequest httpRequest) {
		if (!secretKey.equals(httpRequest.getHeader("X-API-KEY"))) {
			throw new ApplicationException(FingerPrintStatusCode.INVALID_API_KEY);
		}

		if (!request.studentId().equals("0000") && fingerPrintRepository.existsByStudentId(request.studentId())) {
			throw new ApplicationException(FingerPrintStatusCode.STUDENT_ID_ALREADY_EXISTS);
		}

		FingerPrint fingerPrint = FingerPrint.builder()
				.studentId(request.studentId())
				.encrypted_template(request.encrypted_template())
				.build();

		fingerPrintRepository.save(fingerPrint);
		return ApiResponse.ok(HttpStatus.CREATED);
	}

	public ApiResponse<List<FingerPrintResponse>> read(HttpServletRequest request) {
		if (!secretKey.equals(request.getHeader("X-API-KEY"))) {
			throw new ApplicationException(FingerPrintStatusCode.INVALID_API_KEY);
		}

		List<FingerPrint> fingerPrint = fingerPrintRepository.findAll();
		return ApiResponse.ok(FingerPrintResponse.toList(fingerPrint));
	}
}
