package com.spring.slik_v2_server.domain.attendance.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import com.spring.slik_v2_server.domain.attendance.entity.AttendanceStatus;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTime;
import com.spring.slik_v2_server.domain.dodam.entity.Dodam;

public record StudentAttendanceResponse(
		String studentId,
		String name,
		AttendanceStatus s1Status,
		LocalTime s1InTime,
		LocalTime s1OutTime,
		AttendanceStatus s2Status,
		LocalTime s2InTime,
		LocalTime s2OutTime,
		LocalDate startDate,
		LocalDate endDate,
		PhoneResponse phone

) {

	public static StudentAttendanceResponse of(AttendanceTime attendanceTime, Dodam dodam) {
		return new StudentAttendanceResponse(
				attendanceTime.getStudent() != null ? attendanceTime.getStudent().getStudentId() : null,
				attendanceTime.getStudent() != null ? attendanceTime.getStudent().getName() : null,
				attendanceTime.getS1Status(),
				attendanceTime.getS1InTime(),
				attendanceTime.getS1OutTime(),
				attendanceTime.getS2Status(),
				attendanceTime.getS2InTime(),
				attendanceTime.getS2OutTime(),
				dodam.getStartAt(),
				dodam.getEndAt(),
				PhoneResponse.of(dodam)
		);
	}
}