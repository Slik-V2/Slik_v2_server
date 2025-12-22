package com.spring.slik_v2_server.domain.fingerprint.controller;

import com.spring.slik_v2_server.domain.fingerprint.dto.request.FingerPrintRequest;
import com.spring.slik_v2_server.domain.fingerprint.service.FingerPrintService;
import com.spring.slik_v2_server.global.data.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class FingerPrintController {

	private final FingerPrintService fingerPrintService;

	@PostMapping("/register")
	public ApiResponse<HttpStatus> create(@RequestBody FingerPrintRequest request) {
		return fingerPrintService.create(request);
	}
}
