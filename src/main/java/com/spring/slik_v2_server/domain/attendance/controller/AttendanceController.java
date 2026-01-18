package com.spring.slik_v2_server.domain.attendance.controller;

import com.spring.slik_v2_server.domain.attendance.dto.request.AttendanceStatusRequest;
import com.spring.slik_v2_server.domain.attendance.dto.request.AttendanceTimeSetRequest;
import com.spring.slik_v2_server.domain.attendance.dto.response.AttendanceTimeSetResponse;
import com.spring.slik_v2_server.domain.attendance.service.AttendanceService;
import com.spring.slik_v2_server.global.data.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AttendanceController {

	private final AttendanceService attendanceService;

	@GetMapping("/schedule")
	public ApiResponse<AttendanceTimeSetResponse> getSchedule() {
		return attendanceService.getSchedule();
	}

	@PutMapping("/schedule")
	public ApiResponse<AttendanceTimeSetResponse> setSchedule(@RequestBody AttendanceTimeSetRequest request) {
		return attendanceService.setSchedule(request);
	}

	@GetMapping("/override/lookup")
	public ApiResponse<?> readAttendanceStatus(@RequestBody AttendanceStatusRequest request) {
		return attendanceService.findAttendanceStatus(request.student_id());
	}
}