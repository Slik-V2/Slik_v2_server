package com.spring.slik_v2_server.domain.fingerprint.controller;

import com.spring.slik_v2_server.domain.fingerprint.dto.request.FingerPrintRequest;
import com.spring.slik_v2_server.domain.fingerprint.dto.response.FingerPrintResponse;
import com.spring.slik_v2_server.domain.fingerprint.service.FingerPrintService;
import com.spring.slik_v2_server.domain.student.service.StudentService;
import com.spring.slik_v2_server.global.data.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class FingerPrintController {

	private final FingerPrintService fingerPrintService;
	private final StudentService studentService;

	@PostMapping("/register")
	public ApiResponse<HttpStatus> create(@Valid @RequestBody FingerPrintRequest request, HttpServletRequest httpRequest) {
		fingerPrintService.create(request, httpRequest);
		return studentService.saveStudentInfo(request.studentId());
	}

	@GetMapping("/manage")
	public ApiResponse<List<FingerPrintResponse>> read(HttpServletRequest request) {
		return fingerPrintService.read(request);
	}
}