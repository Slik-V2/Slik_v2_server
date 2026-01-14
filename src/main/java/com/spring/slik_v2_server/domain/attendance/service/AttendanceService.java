package com.spring.slik_v2_server.domain.attendance.service;

import com.spring.slik_v2_server.domain.attendance.dto.request.AttendanceTimeRequest;
import com.spring.slik_v2_server.domain.attendance.dto.response.AttendanceTimeResponse;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTime;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTimeEnum;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceType;
import com.spring.slik_v2_server.domain.attendance.repository.AttendanceRepository;
import com.spring.slik_v2_server.domain.fingerprint.entity.FingerPrint;
import com.spring.slik_v2_server.domain.fingerprint.exception.FingerPrintStatusCode;
import com.spring.slik_v2_server.domain.fingerprint.repository.FingerPrintRepository;
import com.spring.slik_v2_server.global.data.ApiResponse;
import com.spring.slik_v2_server.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

	private final AttendanceRepository attendanceRepository;
	private final FingerPrintRepository fingerPrintRepository;
	@Value("${spring.Dodam.API_KEY}")
	private String DodamApiKey;

	// 심자 출석체크 가능 시간 설정
	public ApiResponse<AttendanceTimeResponse> setSchedule (AttendanceTimeRequest request) {
		AttendanceType type = request.attendanceType();

		AttendanceTimeEnum defaultTime = type.getDefaultTime();

		LocalTime startTime = request.startTime() != null ? request.startTime() : defaultTime.getDefaultStartTime();
		LocalTime endTime = request.endTime() != null ? request.endTime() : defaultTime.getDefaultEndTime();

		AttendanceTime attendanceTime = attendanceRepository.findByType(type).map(exist -> {
				exist.updateTime(startTime, endTime);
		return exist;
		}).orElseGet(() -> AttendanceTime.builder()
				.type(request.attendanceType())
				.today(LocalDate.now())
				.startTime(startTime)
				.endTime(endTime)
				.build());

		attendanceRepository.save(attendanceTime);
		return ApiResponse.ok(AttendanceTimeResponse.of(attendanceTime));
	}

	public ApiResponse<List<AttendanceTimeResponse>> findAttendanceStatus(String id) {
		LocalDate today = LocalDate.now();

		FingerPrint fingerPrint = fingerPrintRepository.findByStudentId(id)
				.orElseThrow(() -> new ApplicationException(FingerPrintStatusCode.STUDENT_NOT_FOUND));

		List<AttendanceTime> attendanceTimes = attendanceRepository.findAllByFingerPrintAndTodayBetween(fingerPrint, today, today);
		List<AttendanceTimeResponse> attendanceStatus = AttendanceTimeResponse.fromList(attendanceTimes);
		return ApiResponse.ok(attendanceStatus);
	}
}
