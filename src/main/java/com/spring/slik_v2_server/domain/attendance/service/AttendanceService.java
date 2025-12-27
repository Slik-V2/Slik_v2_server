package com.spring.slik_v2_server.domain.attendance.service;

import com.spring.slik_v2_server.domain.attendance.dto.request.AttendanceTimeRequest;
import com.spring.slik_v2_server.domain.attendance.dto.response.AttendanceTimeResponse;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTime;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTimeEnum;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceType;
import com.spring.slik_v2_server.domain.attendance.repository.AttendanceRepository;
import com.spring.slik_v2_server.global.data.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class AttendanceService {

	private final AttendanceRepository attendanceRepository;

	// 심자 출석체크 가능 시간 설정
	public ApiResponse<AttendanceTimeResponse> setAttendanceTime (AttendanceTimeRequest request) {
		AttendanceType type = request.attendanceType();

		AttendanceTimeEnum defaultTime = type.getDefaultTime();

		LocalTime startTime = request.startTime() != null ? request.startTime() : defaultTime.getDefaultStartTime();
		LocalTime endTime = request.endTime() != null ? request.endTime() : defaultTime.getDefaultEndTime();

		AttendanceTime attendanceTime = AttendanceTime.builder()
				.type(request.attendanceType())
				.startTime(startTime)
				.endTime(endTime)
				.build();

		attendanceRepository.save(attendanceTime);
		return ApiResponse.ok(AttendanceTimeResponse.of(attendanceTime));
	}
}
