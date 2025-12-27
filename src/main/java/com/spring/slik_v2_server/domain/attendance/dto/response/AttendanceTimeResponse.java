package com.spring.slik_v2_server.domain.attendance.dto.response;

import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTime;

import java.time.LocalTime;

public record AttendanceTimeResponse(
		LocalTime startTime,
		LocalTime endTime
) {

	public static AttendanceTimeResponse of(AttendanceTime attendanceTime) {
		return new AttendanceTimeResponse(attendanceTime.getStartTime(), attendanceTime.getEndTime());
	}
}
