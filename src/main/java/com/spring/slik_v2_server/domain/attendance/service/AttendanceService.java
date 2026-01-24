package com.spring.slik_v2_server.domain.attendance.service;

import com.spring.slik_v2_server.domain.attendance.dto.request.AttendanceTimeSetRequest;
import com.spring.slik_v2_server.domain.attendance.dto.response.AbsencesResponse;
import com.spring.slik_v2_server.domain.attendance.dto.response.AttendanceTimeResponse;
import com.spring.slik_v2_server.domain.attendance.dto.response.AttendanceTimeSetResponse;
import com.spring.slik_v2_server.domain.attendance.dto.response.CalendarResponse;
import com.spring.slik_v2_server.domain.attendance.dto.response.GetStudentInfoResponse;
import com.spring.slik_v2_server.domain.attendance.dto.response.LiveAttendanceResponse;
import com.spring.slik_v2_server.domain.attendance.dto.response.StudentAttendanceResponse;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceStatus;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTime;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTimeEnum;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTimeSet;
import com.spring.slik_v2_server.domain.attendance.repository.AttendanceRepository;
import com.spring.slik_v2_server.domain.attendance.repository.AttendanceSetRepository;
import com.spring.slik_v2_server.domain.dodam.entity.Type;
import com.spring.slik_v2_server.domain.fingerprint.entity.FingerPrint;
import com.spring.slik_v2_server.domain.fingerprint.exception.FingerPrintStatusCode;
import com.spring.slik_v2_server.domain.fingerprint.repository.FingerPrintRepository;
import com.spring.slik_v2_server.domain.student.entity.Student;
import com.spring.slik_v2_server.domain.student.exception.StudentStatus;
import com.spring.slik_v2_server.domain.student.repository.StudentRepository;
import com.spring.slik_v2_server.global.data.ApiResponse;
import com.spring.slik_v2_server.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {

	private final AttendanceRepository attendanceRepository;
	private final AttendanceSetRepository attendanceSetRepository;
	private final FingerPrintRepository fingerPrintRepository;
	private final StudentRepository studentRepository;
	@Value("${spring.Dodam.API_KEY}")
	private String DodamApiKey;

	// 심자 출석체크 가능 시간 조회
	public ApiResponse<AttendanceTimeSetResponse> getSchedule(LocalDate date) {
		AttendanceTimeSet setTime = attendanceSetRepository.findByToday(date).orElseGet(()
				-> {
			AttendanceTimeSet newTimeSet = AttendanceTimeSet.builder()
					.today(LocalDate.now())
					.session1_1Start(AttendanceTimeEnum.session1_1Start.getDefaultTime())
					.session1_1End(AttendanceTimeEnum.session1_1End.getDefaultTime())
					.session1_2Start(AttendanceTimeEnum.session1_2Start.getDefaultTime())
					.session1_2End(AttendanceTimeEnum.session1_2End.getDefaultTime())
					.session2_1Start(AttendanceTimeEnum.session2_1Start.getDefaultTime())
					.session2_1End(AttendanceTimeEnum.session2_1End.getDefaultTime())
					.session2_2Start(AttendanceTimeEnum.session2_2Start.getDefaultTime())
					.session2_2End(AttendanceTimeEnum.session2_2End.getDefaultTime())
					.build();
			return attendanceSetRepository.save(newTimeSet);
		});

		return ApiResponse.ok(AttendanceTimeSetResponse.of(setTime));
	}

	// 심자 출석체크 가능 시간 설정
	public ApiResponse<AttendanceTimeSetResponse> setSchedule (AttendanceTimeSetRequest request,
															   LocalDate date) {

		// 심자1 출석체크 가능 범위
		LocalTime session1_1Start = setDefault(
				request.session1_1Start(),
				AttendanceTimeEnum.session1_1Start
		);
		LocalTime session1_1End = setDefault(
				request.session1_1End(),
				AttendanceTimeEnum.session1_1End
		);

		// 심자1 복귀체크 가능 범위
		LocalTime session1_2Start = setDefault(
				request.session1_2Start(),
				AttendanceTimeEnum.session1_2Start
		);
		LocalTime session1_2End = setDefault(
				request.session1_2End(),
				AttendanceTimeEnum.session1_2End);

		// 심자2 출석체크 가능 범위
		LocalTime session2_1Start = setDefault(
				request.session2_1Start(),
				AttendanceTimeEnum.session2_1Start
		);
		LocalTime session2_1End = setDefault(
				request.session2_1End(),
				AttendanceTimeEnum.session2_1End
		);

		// 심자2 복귀체크 가능 범위
		LocalTime session2_2Start = setDefault(
				request.session2_2Start(),
				AttendanceTimeEnum.session2_2Start
		);
		LocalTime session2_2End = setDefault(
				request.session2_2End(),
				AttendanceTimeEnum.session2_2End
		);


		AttendanceTimeSet attendanceTimeSet = attendanceSetRepository.findByToday(date).map(exist -> {
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
						.today(date)
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
		List<AttendanceTime> attendanceTimes = attendanceRepository.findAllByToday(today);
		List<StudentAttendanceResponse> students = attendanceTimes.stream()
				.map(StudentAttendanceResponse::of)
				.collect(Collectors.toList());
		return ApiResponse.ok(LiveAttendanceResponse.of(today, students));
	}

	public ApiResponse<?> getStudentInfo(String studentId) {
		Student student = studentRepository.findByStudentId(studentId)
				.orElseThrow(() -> new ApplicationException(StudentStatus.STUDENT_NOT_FOUND));

		return ApiResponse.ok(GetStudentInfoResponse.of(student));
	}

	public ApiResponse<?> absences(String studentId, int year, int month) {
		Student student = studentRepository.findByStudentId(studentId)
				.orElseThrow(() -> new ApplicationException(StudentStatus.STUDENT_NOT_FOUND));

		LocalDate startDate = LocalDate.of(year, month, 1);
		LocalDate endDate = startDate.withDayOfMonth(startDate.getMonth().length(startDate.isLeapYear()));

		List<AttendanceTime> attendanceTimes = attendanceRepository.findAllByStudentAndTodayBetween(student, startDate, endDate);
		List<AbsencesResponse> responses = AbsencesResponse.fromList(attendanceTimes);
		return ApiResponse.ok(responses);
	}

	public ApiResponse<?> calendar(int year, int month, String studentId) {
		Student student = studentRepository.findByStudentId(studentId)
				.orElseThrow(() -> new ApplicationException(StudentStatus.STUDENT_NOT_FOUND));

		LocalDate startDate = LocalDate.of(year, month, 1);
		LocalDate endDate = startDate.withDayOfMonth(startDate.getMonth().length(startDate.isLeapYear()));

		List<AttendanceTime> attendanceTimes = attendanceRepository.findAllByStudentAndTodayBetween(
				student, startDate, endDate
		);

		Map<LocalDate, AttendanceTime> dateMap = attendanceTimes.stream()
				.collect(Collectors.toMap(AttendanceTime::getToday, a -> a));

		Map<Integer, String> calendar = new HashMap<>();
		for (int day = 1; day <= endDate.getDayOfMonth(); day++) {
			LocalDate date = LocalDate.of(year, month, day);
			AttendanceTime attendance = dateMap.get(date);
			calendar.put(day, judgeAttendanceStatus(attendance));
		}

		return ApiResponse.ok(new CalendarResponse(calendar));
	}

	private String judgeAttendanceStatus(AttendanceTime attendance) {
		// 데이터가 없으면 null
		if (attendance == null) {
			return null;
		}

		Type type = attendance.getType();

		if (type == Type.NIGHT_STUDY_1) {
			AttendanceStatus s1Status = attendance.getS1Status();
			if (s1Status == AttendanceStatus.RETURNED) {
				return "present";
			}
			if (s1Status == AttendanceStatus.STUDYING || s1Status == AttendanceStatus.NONE) {
				return "absent";
			}
		}

		if (type == Type.NIGHT_STUDY_2) {
			AttendanceStatus s2Status = attendance.getS2Status();

			if (s2Status == AttendanceStatus.RETURNED) {
				return "present";
			}

			if (s2Status == AttendanceStatus.STUDYING || s2Status == AttendanceStatus.NONE) {
				return "absent";
			}
		}

		return null;
	}

}