package com.spring.slik_v2_server.domain.attendance.service;

import com.spring.slik_v2_server.domain.attendance.dto.request.AttendanceTimeSetRequest;
import com.spring.slik_v2_server.domain.attendance.dto.response.AttendanceTimeResponse;
import com.spring.slik_v2_server.domain.attendance.dto.response.AttendanceTimeSetResponse;
import com.spring.slik_v2_server.domain.attendance.dto.response.LiveAttendanceResponse;
import com.spring.slik_v2_server.domain.attendance.dto.response.StudentAttendanceResponse;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTime;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTimeEnum;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTimeSet;
import com.spring.slik_v2_server.domain.attendance.repository.AttendanceRepository;
import com.spring.slik_v2_server.domain.attendance.repository.AttendanceSetRepository;
import com.spring.slik_v2_server.domain.fingerprint.entity.FingerPrint;
import com.spring.slik_v2_server.domain.fingerprint.exception.FingerPrintStatusCode;
import com.spring.slik_v2_server.domain.fingerprint.repository.FingerPrintRepository;
import com.spring.slik_v2_server.domain.student.entity.Student;
import com.spring.slik_v2_server.domain.student.repository.StudentRepository;
import com.spring.slik_v2_server.global.data.ApiResponse;
import com.spring.slik_v2_server.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {

	private final AttendanceRepository attendanceRepository;
	private final AttendanceSetRepository attendanceSetRepository;
	private final FingerPrintRepository fingerPrintRepository;
	@Value("${spring.Dodam.API_KEY}")
	private String DodamApiKey;

	// 심자 출석체크 가능 시간 조회
	public ApiResponse<AttendanceTimeSetResponse> getSchedule() {
		AttendanceTimeSet setTime = attendanceSetRepository.findByToday(LocalDate.now()).orElseThrow();
		return ApiResponse.ok(AttendanceTimeSetResponse.of(setTime));
	}

	// 심자 출석체크 가능 시간 설정
	public ApiResponse<AttendanceTimeSetResponse> setSchedule (AttendanceTimeSetRequest request) {

		LocalTime session1_1Start = setDefault(request.session1_1Start(), AttendanceTimeEnum.session1_1Start);
		LocalTime session1_1End = setDefault(request.session1_1End(), AttendanceTimeEnum.session1_1End);

		LocalTime session1_2Start = setDefault(request.session1_2Start(), AttendanceTimeEnum.session1_2Start);
		LocalTime session1_2End = setDefault(request.session1_2End(), AttendanceTimeEnum.session1_2End);

		LocalTime session2_1Start = setDefault(request.session2_1Start(), AttendanceTimeEnum.session2_1Start);
		LocalTime session2_1End = setDefault(request.session2_1End(), AttendanceTimeEnum.session2_1End);

		LocalTime session2_2Start = setDefault(request.session2_2Start(), AttendanceTimeEnum.session2_2Start);
		LocalTime session2_2End = setDefault(request.session2_2End(), AttendanceTimeEnum.session2_2End);

		AttendanceTimeSet attendanceTimeSet = attendanceSetRepository.findByToday(LocalDate.now()).map(exist -> {
			exist.updateTime(
					session1_1Start,
					session1_1End,
					session1_2Start,
					session1_2End,
					session2_1Start,
					session2_1End,
					session2_2Start,
					session2_2End);

			return exist;
		}).orElseGet(() ->
				AttendanceTimeSet.builder()
						.session1_1Start(session1_1Start)
						.session1_1End(session1_1End)
						.session1_2Start(session1_2Start)
						.session1_2End(session1_2End)
						.session2_1Start(session2_1Start)
						.session2_1End(session2_1End)
						.session2_2Start(session2_2Start)
						.session2_2End(session2_2End)
						.build()
		);

		attendanceSetRepository.save(attendanceTimeSet);
		return ApiResponse.ok(AttendanceTimeSetResponse.of(attendanceTimeSet));
	}

	public ApiResponse<List<AttendanceTimeResponse>> findAttendanceStatus(String id) {
		LocalDate today = LocalDate.now();

		FingerPrint fingerPrint = fingerPrintRepository.findByStudentId(id)
				.orElseThrow(() -> new ApplicationException(FingerPrintStatusCode.STUDENT_NOT_FOUND));

		List<AttendanceTime> attendanceTimes = attendanceRepository.findAllByFingerPrintAndTodayBetween(fingerPrint, today, today);
		List<AttendanceTimeResponse> attendanceStatus = AttendanceTimeResponse.fromList(attendanceTimes);
		return ApiResponse.ok(attendanceStatus);
	}

	private LocalTime setDefault(LocalTime inputTime, AttendanceTimeEnum time) {
		return inputTime != null ? inputTime : time.getDefaultTime();
	}

	public ApiResponse<?> getliveAttendanceStatus(LocalDate today) {
		List<?> students = attendanceRepository.findAllbyToday(today);
	}
}