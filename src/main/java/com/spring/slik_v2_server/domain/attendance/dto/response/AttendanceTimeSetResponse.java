package com.spring.slik_v2_server.domain.attendance.dto.response;

import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTimeSet;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public record AttendanceTimeSetResponse (
		String session1_1Start,
		String session1_1End,

		String session1_2Start,
		String session1_2End,

		String session2_1Start,
		String session2_1End,

		String session2_2Start,
		String session2_2End
) {
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

	private static String formatTime(LocalTime time) {
		return time != null ? time.format(TIME_FORMATTER) : "";
	}

	public static AttendanceTimeSetResponse of(AttendanceTimeSet attendanceTimeSet) {
		return new AttendanceTimeSetResponse(
				formatTime(attendanceTimeSet.getSession1_1Start()),
				formatTime(attendanceTimeSet.getSession1_1End()),
				formatTime(attendanceTimeSet.getSession1_2Start()),
				formatTime(attendanceTimeSet.getSession1_2End()),
				formatTime(attendanceTimeSet.getSession2_1Start()),
				formatTime(attendanceTimeSet.getSession2_1End()),
				formatTime(attendanceTimeSet.getSession2_2Start()),
				formatTime(attendanceTimeSet.getSession2_2End())
		);
	}
}
