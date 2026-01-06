package com.spring.slik_v2_server.domain.attendance.dto.response;

import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTime;

import java.time.LocalDate;
import java.time.LocalTime;

public record AttendanceTimeResponse(
		LocalDate today,
		LocalTime startTime,
		LocalTime endTime
) {

	public static AttendanceTimeResponse of(AttendanceTime attendanceTime) {
		return new AttendanceTimeResponse(attendanceTime.getToday(), attendanceTime.getStartTime(), attendanceTime.getEndTime());
	}
}
