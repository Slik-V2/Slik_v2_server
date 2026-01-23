package com.spring.slik_v2_server.domain.attendance.controller;

import com.spring.slik_v2_server.domain.attendance.dto.request.AttendanceStatusRequest;
import com.spring.slik_v2_server.domain.attendance.dto.request.AttendanceTimeSetRequest;
import com.spring.slik_v2_server.domain.attendance.dto.response.AttendanceTimeResponse;
import com.spring.slik_v2_server.domain.attendance.dto.response.AttendanceTimeSetResponse;
import com.spring.slik_v2_server.domain.attendance.service.AttendanceService;
import com.spring.slik_v2_server.global.data.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AttendanceController {

	private final AttendanceService attendanceService;

	@GetMapping("/schedule/{date}")
	public ApiResponse<AttendanceTimeSetResponse> getSchedule(
			@PathVariable("date")
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate) {
		return attendanceService.getSchedule(localDate);
	}

	@PutMapping("/schedule/{date}")
	public ApiResponse<AttendanceTimeSetResponse> setSchedule(@RequestBody AttendanceTimeSetRequest request,
															  @PathVariable("date")
															  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate) {
		return attendanceService.setSchedule(request, localDate);
	}

	@GetMapping("/override/lookup")
	public ApiResponse<List<AttendanceTimeResponse>> readAttendanceStatus(@RequestBody AttendanceStatusRequest request) {
		return attendanceService.findAttendanceStatus(request.student_id());
	}

	@GetMapping("/attendance/live")
	public ApiResponse<?> getLiveAttendanceStatus(
			@RequestParam LocalDate date
			) {
		return attendanceService.getliveAttendanceStatus(date);
	}

	@GetMapping("/students/{studentId}")
	public ApiResponse<?> getStudentInfo(
			@PathVariable String studentId
	) {
		return attendanceService.getStudentInfo(studentId);
	}

	@GetMapping("/students/{studentId}/absences")
	public ApiResponse<?> absences(
			@PathVariable String studentId,
			@RequestParam int year,
			@RequestParam int month) {
		return attendanceService.absences(studentId, year, month);
	}

	@GetMapping("/students/{studentId}/attendance")
	public ApiResponse<?> calendar(
			@PathVariable String studentId,
			@RequestParam int year,
			@RequestParam int month) {
		return attendanceService.calendar(year, month, studentId);
	}
}