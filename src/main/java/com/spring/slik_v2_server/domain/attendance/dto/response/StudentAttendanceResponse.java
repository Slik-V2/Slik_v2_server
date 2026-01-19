package com.spring.slik_v2_server.domain.attendance.dto.response;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.spring.slik_v2_server.domain.attendance.entity.AttendanceStatus;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTime;

import com.fasterxml.jackson.annotation.JsonInclude;

public record StudentAttendanceResponse(
		String studentId,
		String name,
		AttendanceStatus s1Status,
		LocalTime s1InTime,
		LocalTime s1OutTime,
		AttendanceStatus s2Status,
		LocalTime s2InTime,
		LocalTime s2OutTime
) {

	public static StudentAttendanceResponse of(AttendanceTime attendanceTime) {
		return new StudentAttendanceResponse(
				attendanceTime.getFingerPrint().getStudentId(),
				attendanceTime.getStudent().getName(),
				attendanceTime.getS1Status(),
				attendanceTime.getS1InTime(),
				attendanceTime.getS1OutTime(),
				attendanceTime.getS2Status(),
				attendanceTime.getS2InTime(),
				attendanceTime.getS2OutTime()
		);
	}
}