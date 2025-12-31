package com.spring.slik_v2_server.domain.dodam.controller;

import com.spring.slik_v2_server.domain.dodam.dto.response.SaveStudentsResponse;
import com.spring.slik_v2_server.domain.dodam.dto.response.external.NightStudyResponse;
import com.spring.slik_v2_server.domain.dodam.entity.Dodam;
import com.spring.slik_v2_server.domain.dodam.service.DodamService;
import com.spring.slik_v2_server.global.data.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NightStudyController {

	private final DodamService dodamService;

	@PostMapping("/night_study_list")
	public ApiResponse<SaveStudentsResponse> saveStudents(@RequestBody NightStudyResponse response) {

		List<Dodam> savedStudents = dodamService.saveStudents(response);
		return ApiResponse.ok(SaveStudentsResponse.of(savedStudents));
	}
}
