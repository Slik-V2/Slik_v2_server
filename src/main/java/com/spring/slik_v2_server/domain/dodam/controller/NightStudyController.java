package com.spring.slik_v2_server.domain.dodam.controller;

import java.util.List;

import com.spring.slik_v2_server.domain.device.service.AttendanceQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.slik_v2_server.domain.dodam.dto.response.SaveStudentsResponse;
import com.spring.slik_v2_server.domain.dodam.entity.Dodam;
import com.spring.slik_v2_server.domain.dodam.service.DodamService;
import com.spring.slik_v2_server.global.data.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Slf4j
public class NightStudyController {
	private final AttendanceQueryService attendanceQueryServiceservice;
	private final DodamService dodamService;

	@PostMapping("/night_study_list")
	public ApiResponse<SaveStudentsResponse> saveStudents() {

		List<Dodam> savedStudents = dodamService.saveStudent();
		return ApiResponse.ok(SaveStudentsResponse.of(savedStudents));
	}

	@GetMapping("/attendanceTime")
	public void saveStatus() {
		log.info("일일 출석 기록 생성 스케줄러 시작");

		try {
			attendanceQueryServiceservice.createDailyAttendanceRecords();
			log.info("일일 출석 기록이 정상적으로 생성되었습니다.");
		} catch (Exception e) {
			log.error("일일 출석 기록 생성 중 오류가 발생했습니다.", e);
		}
	}
}
